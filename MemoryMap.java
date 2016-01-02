/**
 *  Name:       Paul Pelayo (pp1286)
 *  Class:      Operating Systems
 *  Professor:  Allan Gottlieb
 *  Due Date:   9/17/15
 *
 *  MemoryMap object used to collect data about a word from the program text
 *  and correctly handle it.  Will also process some errors
 *
 */

import java.util.ArrayList;

/**
 * Created by PaulPelayo on 9/15/15.
 */
public class MemoryMap {

    final static int MACHINE_SIZE = 300;

    private int word;
    private int offset;
    private int addressType;
    private int wordUpdate;
    private int position;
    private int var;
    private ModuleData module;
    private String sym;
    private int errorType;          // 1 if it is a symbol undefined error
                                    // 2 if it is a duplication error



    public MemoryMap(String sym, int word, int offset, int position, int var, ModuleData module){
        this.word = word;
        this.offset = offset;
        this.position = position;
        this.var = var;
        this.module = module;
        this.sym = sym;
        addressType = word % 10;
        wordUpdate = word / 10;
    }

    public MemoryMap(String sym, int word, int offset, int position, int var, ModuleData module, int errorType){
        this.word = word;
        this.offset = offset;
        this.position = position;
        this.var = var;
        this.module = module;
        this.errorType = errorType;
        this.sym = sym;
        addressType = word % 10;
        wordUpdate = word / 10;
    }

    public int getOldWord(){
        //for testing purposes
        return word;
    }

    public String getNewAddress(){
        if (addressType == 1) {
            //immediate
            return "" + wordUpdate;
        }
        else if (addressType == 2){
            //absolute
            if (wordUpdate % 1000 > MACHINE_SIZE)
                return "" + ((wordUpdate/1000) * 1000 + 299) + " Error: Absolute address exceeds machine size; largest address used.";
            return "" + wordUpdate;
        }
        else if (addressType == 3){
            //relative
            //System.out.println(wordUpdate);
            //System.out.println(wordUpdate % 1000);
            //System.out.println(module.getModuleLines());
            if (wordUpdate % 1000 > module.getModuleLines()){
                return "" + ((wordUpdate/1000) * 1000 + module.getMaxModuleAddress()) + " Error: Relative address exceeds module size; largest module address used.";
            }
            return  "" + (wordUpdate + offset);
        }
        else {
            if (errorType == 1)
                return "" + ((wordUpdate/1000) * 1000 + var) + " Error: " + sym + " is not defined; 111 used.";
            else if (errorType == 2)
                return "" + ((wordUpdate/1000) * 1000 + var) + " Error: Multiple variables used in instruction; all but last ignored.";
            else return "" + ((wordUpdate/1000) * 1000 + var);
        }



    }




}
