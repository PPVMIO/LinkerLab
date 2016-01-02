/**
 *  Name:       Paul Pelayo (pp1286)
 *  Class:      Operating Systems
 *  Professor:  Allan Gottlieb
 *  Due Date:   9/17/15
 *
 *  ModuleData object used to collect data about a module
 *
 */

/**
 * Created by PaulPelayo on 9/16/15.
 */
public class ModuleData {



    private int moduleLines;
    private int moduleNumber;
    private int maxModuleAddress;


    public ModuleData() {
        this.moduleLines = 0;
        this.moduleNumber = 0;
    }

    public ModuleData(int programLines, int moduleNumber){
        this.moduleLines = programLines;
        this.moduleNumber = moduleNumber;

    }


    public int getModuleLines() {
        return moduleLines;
    }


    public int getMaxModuleAddress(){
        return maxModuleAddress;
    }


    public void setModuleLines(int programLines) {
        this.moduleLines = programLines;
    }

    public void setModuleNumber(int moduleNumber) {
        this.moduleNumber = moduleNumber;
    }

    public void setMaxModuleAddress(int moduleAddress){
        this.maxModuleAddress = moduleAddress;
    }






}
