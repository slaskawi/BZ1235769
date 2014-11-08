package org.infinispan.wfink.filestore;

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

import org.infinispan.marshall.core.JBossMarshaller;

/**
 * Attempt to read a *.dat file written by an Infinispan SingleFileStore.
 * It will show a statistic of the used/unused entries and
 * show the key/data bytes.
 * TODO unmarshall the data
 * 
 * @author Wolf-Dieter Fink
 */
public class SingleFileStoreReader {
   // from SingleFileStor implementation
   private static final byte[] MAGIC = new byte[]{'F', 'C', 'S', '1'};
   private static final int KEY_POS = 4 + 4 + 4 + 4 + 8;

   private final File filestore;
   private final boolean showKey;
   private final boolean showData;
   private final boolean noMeta;
   
   private final JBossMarshaller marshaller = new JBossMarshaller();
   
   private long usedEntries = 0;
   private long freeEntries = 0;
   private long usedSize = 0;
   private long usedKeySize = 0;
   private long usedDataSize = 0;
   private long unusedSize = 0;

   public SingleFileStoreReader(File fileStore, boolean noMeta, boolean showKey, boolean showData) {
      this.filestore = fileStore;
      this.showKey = showKey;
      this.showData = showData;
      this.noMeta = noMeta;
   }

   private void show() throws IOException {
      FileChannel channel = new RandomAccessFile(this.filestore, "r").getChannel();
      
      try {
         // check file format and read persistent state if enabled for the cache
         byte[] header = new byte[MAGIC.length];
         if (channel.read(ByteBuffer.wrap(header), 0) == MAGIC.length && Arrays.equals(MAGIC, header)) {
            try {
               read(channel);
               showSummary();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }else{
            throw new RuntimeException("Format of File "+filestore+" unknown!");
         }
      } finally {
         channel.close();
      }
   }
   
   /**
    * Rebuilds the in-memory index from file.
    */
   private void read(FileChannel channel) throws Exception {
      long filePos = MAGIC.length;
      ByteBuffer buf = ByteBuffer.allocate(KEY_POS);
      for (; ; ) {
         // read FileEntry fields from file (size, keyLen etc.)
         buf.clear().limit(KEY_POS);
         channel.read(buf, filePos);
         // return if end of file is reached
         if (buf.remaining() > 0)
            return;
         buf.flip();

         // initialize FileEntry from buffer
         int entrySize = buf.getInt();
         int keyLen = buf.getInt();
         int dataLen = buf.getInt();
         int metadataLen = buf.getInt();
         long expiryTime = buf.getLong();
         FileEntry fe = new FileEntry(filePos, entrySize, keyLen, dataLen, metadataLen, expiryTime);

         // sanity check
         if (fe.size < KEY_POS + fe.keyLen + fe.dataLen + fe.metadataLen) {
            throw new RuntimeException("ERROR reading FileStore '" + filestore.getPath() + "' at pos=" + filePos);
         }

         // update file pointer
         filePos += fe.size;

         // check if the entry is used or free
         if (fe.keyLen > 0) {
            showEntry(fe);
            
            if(showKey) {
               // load the key from file
               if (buf.capacity() < fe.keyLen)
                  buf = ByteBuffer.allocate(fe.keyLen);
   
               buf.clear().limit(fe.keyLen);
               channel.read(buf, fe.offset + KEY_POS);
   
               // deserialize key
               System.out.printf("   KEY    %s\n",unmarshall(buf.array(), 0, fe.keyLen));
            }
            if(showData) {
               if (buf.capacity() < fe.dataLen)
                  buf = ByteBuffer.allocate(fe.dataLen);
   
               buf.clear().limit(fe.dataLen);
               channel.read(buf, fe.offset + KEY_POS + fe.keyLen);
               System.out.printf("   DATA   %s\n", unmarshall(buf.array(), 0, fe.dataLen));
            }
         } else {
            showEmpty(fe);
         }
      }
   }
   
   /**
    * extract the version information and use the default Marshaller (JBossMarshaller) to show the BYTES human readable 
    */
   private Object unmarshall(byte[] bytes, int offset, int len) throws IOException, ClassNotFoundException {
      ByteArrayInputStream is = new ByteArrayInputStream(bytes, offset, len);
      ObjectInput in = this.marshaller.startObjectInput(is, false);
      int versionId;
      try {
         versionId = in.readShort();
      }
      catch (Exception e) {
         this.marshaller.finishObjectInput(in);
         throw new IOException("Unable to read version id from first two bytes of stream: " + e.getMessage());
      }
      Object o = null;
      try {
         o = this.marshaller.objectFromObjectStream(in);
      } finally {
         this.marshaller.finishObjectInput(in);
      }

      return "'" + o.toString() + "'  C:"+ o.getClass().getSimpleName()+"  V:"+versionId;
   }
   
   private void showEntry(FileEntry entry) {
      if(this.noMeta) return;
      System.out.printf("size %10d key/data %10d/%10d  meta %10d  expire %d\n", entry.size, entry.keyLen, entry.dataLen, entry.metadataLen, entry.expiryTime);
      usedEntries++;
      usedSize += entry.size;
      usedKeySize += entry.keyLen;
      usedDataSize += entry.dataLen;
   }

   private void showEmpty(FileEntry entry) {
      if(this.noMeta) return;
      System.out.printf("size %10d EMPTY\n", entry.size);
      freeEntries++;
      unusedSize += entry.size;
   }

   private void showSummary() {
      System.out.printf("Used     :  %d entries   Sizes: key %10d data %10d   total %10d\n", usedEntries, usedKeySize, usedDataSize, usedSize);
      System.out.printf("Unused   :  %d entries total size %d\n", freeEntries, unusedSize);
   }
   public static void main(String[] args) throws IOException {
      File file = null;
      boolean showKey = false;
      boolean showData = false;
      boolean noMeta = false;

      int argc = 0;
      while (argc < args.length) {
         if (args[argc].startsWith("-")) {
            if(args[argc].equals("-show-key")) {
               showKey = true;
               argc++;
            }else if(args[argc].equals("-show-data")) {
               showData = true;
               argc++;
            }else if(args[argc].equals("-no-meta")) {
               noMeta = true;
               argc++;
            } else {
               System.out.printf("option '%s' unknown\n", args[argc]);
               System.exit(1);
            }
         } else {
            file = new File(args[argc]);
            if(!file.exists() || !file.isFile() || !file.canRead()) {
               System.out.printf("File '%s' does not exists or can not be read!", args[argc]);
               System.exit(1);
            }
            argc++;
         }
      }
      
      if(file == null) {
         System.out.printf("No file given\n");
         System.exit(1);
      }

      SingleFileStoreReader main = new SingleFileStoreReader(file, noMeta, showKey, showData);
      main.show();
   }
   
   
   
   
   
   
   /**
    * Helper class to represent an entry in the cache file.
    * <p/>
    * The format of a FileEntry on disk is as follows:
    * <ul>
    * <li>4 bytes: {@link #size}</li>
    * <li>4 bytes: {@link #keyLen}, 0 if the block is unused</li>
    * <li>4 bytes: {@link #dataLen}</li>
    * <li>4 bytes: {@link #metadataLen}</li>
    * <li>8 bytes: {@link #expiryTime}</li>
    * <li>{@link #keyLen} bytes: serialized key</li>
    * <li>{@link #dataLen} bytes: serialized data</li>
    * <li>{@link #metadataLen} bytes: serialized key</li>
    * </ul>
    */
   private static class FileEntry implements Comparable<FileEntry> {
      /**
       * File offset of this block.
       */
      private final long offset;

      /**
       * Total size of this block.
       */
      private final int size;

      /**
       * Size of serialized key.
       */
      private final int keyLen;

      /**
       * Size of serialized data.
       */
      private final int dataLen;

      /**
       * Size of serialized metadata.
       */
      private final int metadataLen;

      /**
       * Time stamp when the entry will expire (i.e. will be collected by purge).
       */
      private final long expiryTime;
      public FileEntry(long offset, int size, int keyLen, int dataLen, int metadataLen, long expiryTime) {
         this.offset = offset;
         this.size = size;
         this.keyLen = keyLen;
         this.dataLen = dataLen;
         this.metadataLen = metadataLen;
         this.expiryTime = expiryTime;
      }

      public int actualSize() {
         return KEY_POS + keyLen + dataLen + metadataLen;
      }

      @Override
      public int compareTo(FileEntry fe) {
         // We compare the size first, as the entries in the free list must be sorted by size
         int diff = size - fe.size;
         if (diff != 0) return diff;
         return (offset < fe.offset) ? -1 : ((offset == fe.offset) ? 0 : 1);
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) return true;
         if (o == null || getClass() != o.getClass()) return false;

         FileEntry fileEntry = (FileEntry) o;

         if (offset != fileEntry.offset) return false;
         if (size != fileEntry.size) return false;

         return true;
      }

      @Override
      public int hashCode() {
         int result = (int) (offset ^ (offset >>> 32));
         result = 31 * result + size;
         return result;
      }

      @Override
      public String toString() {
         return "FileEntry@" +
               offset +
               "{size=" + size +
               ", actual=" + actualSize() +
               '}';
      }
   }

}
