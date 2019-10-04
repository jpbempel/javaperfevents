package com.bempel.perf.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;

public interface CLibrary extends Library {
    CLibrary INSTANCE = (CLibrary) Native.loadLibrary("c", CLibrary.class);

    int syscall(int number, Object... args);

    int ioctl(int fd, long request, Object... args);

    // specific read method to read 64 bits values (struct read_format)
    int read(int fd, long[] values, int size);

    int close(int fd);

    int perror(String s);

    String strerror(int errnum);
}
