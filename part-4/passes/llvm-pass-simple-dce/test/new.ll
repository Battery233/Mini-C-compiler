; ModuleID = 'test.ll'
source_filename = "test.c"
target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-unknown-linux-gnu"

; Function Attrs: noinline nounwind uwtable
define dso_local i32 @foo() #0 {
  %1 = add nsw i32 3, 1
  ret i32 %1
}

; Function Attrs: noinline nounwind uwtable
define dso_local void @foo2() #0 {
  ret void
}

; Function Attrs: noinline nounwind uwtable
define dso_local i32 @main() #0 {
  %1 = call i32 @foo()
  %2 = mul nsw i32 %1, 2
  ret i32 %2
}

attributes #0 = { noinline nounwind uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "min-legal-vector-width"="0" "no-frame-pointer-elim"="true" "no-frame-pointer-elim-non-leaf" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }

!llvm.module.flags = !{!0}
!llvm.ident = !{!1}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{!"clang version 8.0.0 (https://github.com/llvm-mirror/clang 526402a5f11bbdc58bff7c23ada0e0c5287094c8) (https://github.com/llvm-mirror/llvm 5ba6366a246d3078fbb597f6aad41d100fa40bb2)"}
