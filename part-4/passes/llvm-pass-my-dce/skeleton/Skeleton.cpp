#include "llvm/Pass.h"
#include "llvm/IR/Function.h"
#include "llvm/Support/raw_ostream.h"
#include "llvm/IR/LegacyPassManager.h"
#include "llvm/Transforms/IPO/PassManagerBuilder.h"
using namespace llvm;
using namespace std;

namespace {
struct MyDCE : public FunctionPass {
    static char ID;
    MyDCE() : FunctionPass(ID){}

    virtual bool runOnFunction(Function& F) {
        for (Function::iterator bb = F.begin(), e = F.end(); bb != e; ++bb) {
            for (BasicBlock::iterator i = bb->begin(), e = bb->end(); i != e; ++i) {
            }
        }
        errs() << "my DCE  for function: " << F.getName() << "!\n";
        return false;
    }
};
}

char MyDCE::ID = 0;

static void registerSkeletonPass(const PassManagerBuilder&,
    legacy::PassManagerBase& PM) {
    PM.add(new MyDCE());
}

static RegisterStandardPasses
    RegisterMyPass(PassManagerBuilder::EP_EarlyAsPossible,
        registerSkeletonPass);

__attribute__((unused)) static RegisterPass<MyDCE>
    X("live", "Iterative Liveness Analysis dead code elimination");
