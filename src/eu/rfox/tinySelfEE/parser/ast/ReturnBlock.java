package eu.rfox.tinySelfEE.parser.ast;

import eu.rfox.tinySelfEE.vm.CodeContext;

public class ReturnBlock extends Return {
    public ReturnBlock(ASTItem value) {
        super(value);
    }

    @Override
    public String toString() {
        return "ReturnBlock{" +
                "value=" + value +
                '}';
    }

    @Override
    public CodeContext compile(CodeContext context) {
        value.compile(context);
        context.addReturnBlockBytecode();
        return context;
    }
}
