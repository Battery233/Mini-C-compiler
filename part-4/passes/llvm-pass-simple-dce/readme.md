# llvm-pass-simple-dce

Use Git to clone the LLVM sources and Cmake to generate the Makefiles to build LLVM on Linux. A minimum of Cmake 3.4 or later is required.

To run the pass, build the pass first and then:

```
$ ........./build/bin/clang -S -emit-llvm -Xclang -disable-O0-optnone test.c

$ ........./build/bin/opt -load skeleton/libSkeletonPass.so -mem2reg -skeletonpass test.ll -o new.ll -S
```