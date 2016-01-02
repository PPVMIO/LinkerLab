/**
 *  Name:       Paul Pelayo (pp1286)
 *  Class:      Operating Systems
 *  Professor:  Allan Gottlieb
 *  Due Date:   9/17/15
 *
 *  SymbolData object used to collect data about a symbol from the
 *  definitions list
 *
 */

/**
 * Created by PaulPelayo on 9/13/15.
 */
public class SymbolData {

    private String sym;
    private int val;
    private int moduleNum;
    private String error;
    private boolean usage;
    private boolean undefined;
    private boolean varDuplicate;

    public SymbolData(String sym, int val, int moduleNum) {
        this.sym = sym;
        this.val = val;
        this.moduleNum = moduleNum;
        this.error = "";
    }

    public SymbolData(String sym, int val, int moduleNum, String error){
        this.sym = sym;
        this.val = val;
        this.moduleNum = moduleNum;
        this.error = error;
    }

    public SymbolData(String sym, int val, int moduleNum, boolean undefined){
        this.sym = sym;
        this.val = val;
        this.moduleNum = moduleNum;
        this.undefined = undefined;
        this.error = "";
    }

    public String getSymbol(){
        return sym;
    }

    public int getVal(){
        return val;
    }

    public int getModuleNum(){
        return moduleNum;
    }

    public String getError(){
        if (error != null)
            return error;
        else return "";
    }

    public void updateValue(int val){
        System.out.println("updating value to: " + val);
        this.val = val;

    }

    public void updateUsage(boolean isUsed){
        this.usage = isUsed;
    }

    public boolean isUsed(){
        return usage;
    }

    public boolean isUndefined(){
        return undefined;
    }

}
