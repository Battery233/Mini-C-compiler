; ModuleID = 'test.c'
source_filename = "test.c"
target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-unknown-linux-gnu"

; Function Attrs: noinline nounwind uwtable
define dso_local i32 @foo() #0 {
  %1 = alloca i32, align 4
  %2 = alloca i32, align 4
  %3 = alloca i32, align 4
  store i32 3, i32* %1, align 4
  store i32 2, i32* %2, align 4
  %4 = load i32, i32* %1, align 4
  %5 = add nsw i32 %4, 1
  store i32 %5, i32* %1, align 4
  %6 = load i32, i32* %1, align 4
  %7 = load i32, i32* %2, align 4
  %8 = sub nsw i32 %6, %7
  store i32 %8, i32* %3, align 4
  %9 = load i32, i32* %1, align 4
  ret i32 %9
}

; Function Attrs: noinline nounwind uwtable
define dso_local void @foo2() #0 {
  %1 = alloca i32, align 4
  %2 = alloca i32, align 4
  store i32 3, i32* %1, align 4
  %3 = load i32, i32* %1, align 4
  %4 = add nsw i32 %3, 2
  store i32 %4, i32* %2, align 4
  ret void
}

; Function Attrs: noinline nounwind uwtable
define dso_local i32 @main() #0 {
  %1 = alloca i32, align 4
  %2 = alloca i32, align 4
  %3 = alloca i32, align 4
  %4 = alloca i32, align 4
  %5 = alloca i32, align 4
  store i32 0, i32* %1, align 4
  %6 = call i32 @foo()
  store i32 %6, i32* %2, align 4
  %7 = load i32, i32* %2, align 4
  %8 = mul nsw i32 %7, 2
  store i32 %8, i32* %3, align 4
  %9 = load i32, i32* %3, align 4
  %10 = load i32, i32* %2, align 4
  %11 = sub nsw i32 %9, %10
  store i32 %11, i32* %4, align 4
  %12 = load i32, i32* %4, align 4
  %13 = load i32, i32* %2, align 4
  %14 = sdiv i32 %12, %13
  store i32 %14, i32* %5, align 4
  %15 = load i32, i32* %3, align 4
  ret i32 %15
}

attributes #0 = { noinline nounwind uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "min-legal-vector-width"="0" "no-frame-pointer-elim"="true" "no-frame-pointer-elim-non-leaf" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }

!llvm.module.flags = !{!0}
!llvm.ident = !{!1}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{!"clang version 8.0.0 (https://github.com/llvm-mirror/clang 526402a5f11bbdc58bff7c23ada0e0c5287094c8) (https://github.com/llvm-mirror/llvm 5ba6366a246d3078fbb597f6aad41d100fa40bb2)"}
