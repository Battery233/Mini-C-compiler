#include "llvm/Pass.h"
#include "llvm/IR/Function.h"
#include "llvm/Support/raw_ostream.h"
#include "llvm/IR/LegacyPassManager.h"
#include "llvm/Transforms/IPO/PassManagerBuilder.h"
#include "llvm/Transforms/Utils/Local.h"
#include <vector>
using namespace llvm;
using namespace std;

namespace {
struct SimpleDCE: public FunctionPass {

    static char ID;
    SimpleDCE(): FunctionPass(ID) {}

    SmallVector<Instruction*, 64> Worklist;
    bool flag;
    int counter;

    virtual bool runOnFunction(Function & F) {

        errs() << "Function " << F.getName() << "\n";
        counter = 0;
        flag = true;

        while(flag) {
            for (Function::iterator bb = F.begin(), e = F.end(); bb != e; ++bb) {
                for (BasicBlock::iterator i = bb->begin(), e = bb->end(); i != e; ++i) {
                    Instruction *address = &*i;
                    if (isInstructionTriviallyDead(address, nullptr)) {
                        Worklist.push_back(address);
                    }
                }
            }

            if(0==Worklist.size())
                flag = false;

            while(0!=Worklist.size()) {
                Worklist.back()-> eraseFromParent();
                Worklist.pop_back();
                counter++;
            }
        }

        errs() << "Dead code deleted: " << counter << "\n";        
        return false;
    }
};
}

char SimpleDCE::ID = 0;

static void registerSkeletonPass(const PassManagerBuilder &,
                                 legacy::PassManagerBase &PM) {
    PM.add(new SimpleDCE());
}

static RegisterStandardPasses
RegisterMyPass(PassManagerBuilder::EP_EarlyAsPossible,
               registerSkeletonPass);

__attribute__((unused)) static RegisterPass<SimpleDCE>
X("skeletonpass", "Simple dead code elimination");


