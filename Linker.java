/**
 *  Name:       Paul Pelayo (pp1286)
 *  Class:      Operating Systems
 *  Professor:  Allan Gottlieb
 *  Due Date:   9/17/15
 *
 *  This is the main class that runs the 2 pass linker
 *  main function reads in user input looks for a .txt file
 *  with the same name and uses scanners to scan through each
 *  object in order to create a symbol table and memory map
 *
 *
 */


import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by PaulPelayo on 9/8/15.
 */
public class Linker {

    static HashMap<String, SymbolData> symbolTable = new HashMap<String, SymbolData>();     // stores a symbol object that is mapped by its symbol name
    static ArrayList<MemoryMap> memoryMap = new ArrayList<MemoryMap>();                     // stores an object that helps calculate a words memory map
    static String warnings = "";                                                            // warnings to be printed out at the end
    static String extraUsesError = "";                                                      // extra uses error to be printed out at the end
    static ArrayList<ModuleData> modules = new ArrayList<ModuleData>();                              // list of modules and their data
    static int largestModuleAddress = 0;                                                    // largest module address (largest variable)




    public static void firstPass(Scanner input){

        int moduleNum                   =   0;                                              // current module
        int offset                      =   0;                                              // line offset between modules
        ArrayList<String> useList       =   new ArrayList<String>();                        // keeps track of the uses of symbols



        /*
         *  Loop through the a module, returns to the top of while loop
         *  once a module has been looped through entirely (pass 1)
         */
        while (input.hasNext()){

            /*
             *  Handle Definitions List
             */
            int numDefinitionValues = input.nextInt();                                      // number of definitions
            if (numDefinitionValues != 0){
                for (int i = 0; i < numDefinitionValues; i++){
                    String sym = input.next();                                              // symbol name
                    int val = input.nextInt() + offset;                                     // sdd the offset to a module address


                    if (largestModuleAddress < val)
                        largestModuleAddress = val;


                    if (!symbolTable.containsKey(sym)){
                        SymbolData symbol;
                        symbol = new SymbolData(sym, val, moduleNum);
                        symbolTable.put(sym, symbol);
                    }
                    else {
                        String error = "Error: " + sym + " is multiply defined; last value used.";
                        SymbolData symbol = new SymbolData(sym, val, moduleNum, error);
                        symbolTable.put(sym, symbol);
                    }
                }
            }

            /*
             *  Handle Usage List
             */
            int numUsageValues = input.nextInt();                                           // number of use lists
            ArrayList<Integer> uses = new ArrayList<Integer>();
            String nextVal = "";
            String sym = "";
            int useVal = 0;
            for (int i = 0; i < numUsageValues; i++){
                nextVal = input.next();
                sym = nextVal;
                useList.add(nextVal);                                                       // should only add the symbols (add whatever is after -1)
                while (!nextVal.equals("-1")){                                              // looks for the end of a list
                    useVal = input.nextInt();
                    nextVal = "" + useVal;
                    uses.add(new Integer(useVal));
                }
            }


           /*
            *  Handle Program Text
            *  place holder is used just to scan through elements but
            *  not do anything with them
            */
            int numProgValues = input.nextInt();                                            // number of words in program text
            // checks if number of uses exceeds number of words
            for (int i  = 0; i < uses.size(); i++){
                if (uses.get(i) > numProgValues)
                    extraUsesError = "Use of " + sym + " in module " + moduleNum + " exceeds module size; use ignored.";

            }

            // checks for module size error
            for (Map.Entry<String, SymbolData> entry: symbolTable.entrySet()){
                if (entry.getValue().getVal() > (offset + numProgValues)){
                    String error = "Error: Definition exceeds module size; last word in module used.";
                    SymbolData symbol = new SymbolData(entry.getKey(), offset + numProgValues, moduleNum, error);
                    symbolTable.put(entry.getKey(), symbol);

                }
            }

            String placeHolder = "";
            for (int i = 0; i < numProgValues; i++){
                placeHolder = input.next();
            }

            offset += numProgValues;
            modules.add(new ModuleData(numProgValues, moduleNum));
            moduleNum++;

        }


        /*
         *  Look for warnings and add them to the warnings string
         */
        Iterator tableIterator = symbolTable.entrySet().iterator();
        while (tableIterator.hasNext()) {
            Map.Entry currSym = (Map.Entry) tableIterator.next();

            if(!useList.contains(currSym.getKey())) {
                SymbolData sd = (SymbolData) currSym.getValue();
                warnings = warnings + "Warning: " + currSym.getKey() + " was defined in module " + sd.getModuleNum() + " but never used\n";
            }


        }

    }
    public static void secondPass(Scanner input){
        int moduleNum           =   0;                                                      // number of modules in the program also used to represent when
        int offset              =   0;                                                      // offset between modules
        HashMap<Integer, String> useMap       =   new HashMap<Integer, String>();           // stores a symbol name mapped by it's use, found in the use list


        ArrayList<ModuleData> moduleDataList = new ArrayList<ModuleData>();

         /*
         *  Loop through the a module, returns to the top of while loop
         *  once a module has been looped through entirely (pass 2)
         */
        while (input.hasNext()){
            ModuleData md = new ModuleData();

            /*
             *  Handle Definitions List
             *  we do not care about any of the data from here
             */
            int numDefinitionValues = input.nextInt();
            String symTemp = "";                                                            // place holder
            String valTemp = "";                                                            // place holder
            if (numDefinitionValues != 0){
                for (int i = 0; i < numDefinitionValues; i++){
                    symTemp = input.next();
                    valTemp = input.next();
                }
            }



            /*
             *  Handle Usage List
             */
            int numUsageValues = input.nextInt();

            String[] duplicateSymName = new String[numUsageValues];
            String nextVal = "";
            String sym = "";
            for (int i = 0; i < numUsageValues; i++){
                sym = input.next();                                                         // symbol name

                if (!symbolTable.containsKey(sym)){

                    String error = "Error: " + nextVal + " used but not defined, 111 used";
                    SymbolData symbol = new SymbolData(sym, 111, moduleNum, true);
                    symbolTable.put(sym, symbol);
                    //useList.add(nextVal);
                }
                nextVal = input.next();                                                     // should only add the symbols (add whatever is after -1)
                while (!nextVal.equals("-1"))   {                                           // looks for the end of a list
                    if (useMap.containsKey(new Integer(nextVal)))
                        duplicateSymName[i] = sym;
                    useMap.put(new Integer(nextVal), sym);
                    nextVal = input.next();
                }




            }


            /*
             *  Handle Program Text
             */
            int numProgValues = input.nextInt();
            md.setModuleNumber(moduleNum);
            md.setModuleLines(numProgValues);
            moduleDataList.add(md);

            int word = 0;
            for (int i = 0; i < numProgValues; i++){
                word = input.nextInt();

                String var = useMap.get(i);
                boolean isADuplicate = false;
                for (int j = 0; j < duplicateSymName.length; j++){
                    if (duplicateSymName[j] != null && duplicateSymName[j].equals(var))
                        isADuplicate = true;
                }
                MemoryMap memoryWord;
                if (var != null){
                    if (symbolTable.get(var).isUndefined())
                        memoryWord = new MemoryMap(symbolTable.get(var).getSymbol(), word, offset, i, symbolTable.get(var).getVal(), md, 1);
                    else if (isADuplicate)
                        memoryWord = new MemoryMap(symbolTable.get(var).getSymbol(), word, offset, i, symbolTable.get(var).getVal(), md, 2);
                    else memoryWord  = new MemoryMap(symbolTable.get(var).getSymbol(), word, offset, i, symbolTable.get(var).getVal(), md);

                }
                else memoryWord = new MemoryMap(sym, word, offset, i, 0, md);


                memoryMap.add(memoryWord);
            }

            moduleNum++;
            offset += numProgValues;
            useMap.clear();

            for (ModuleData _md : moduleDataList)
                _md.setMaxModuleAddress(largestModuleAddress);



        }
    }

    public static void printSymbolTable(){
        System.out.println("Symbol Table");
        for (Map.Entry<String, SymbolData> entry: symbolTable.entrySet()){
            System.out.println("\t" + entry.getKey() + "=" + entry.getValue().getVal() + "\t" + entry.getValue().getError());
        }
    }

    public static void printMemoryTable(){
        System.out.println("Memory Map");

        for (int i = 0; i < memoryMap.size(); i++){
            System.out.println("\t" + Integer.toString(i) + ":\t" + memoryMap.get(i).getNewAddress());

        }
    }


    public static void main(String args[]) {

        Scanner textLine = new Scanner (System.in);

        System.out.println("Please enter the name of the input:");
        Scanner input1 = null;
        Scanner input2 = null;

        String textFile = textLine.next();

        textFile.trim();
        try {
            input1 = new Scanner(new File(textFile));
        }
        catch (IOException e)
        {
            System.out.println("File Error: " + textFile + " was not found " + e);
        }
        try {
            input2 = new Scanner(new File(textFile));
        }
        catch (IOException e)
        {
            System.out.println("File Error: " + textFile + " was not found"+ e);
        }
        System.out.println("*************************************************\n");
        System.out.println("~~The output from " + textFile + "~~");
        firstPass(input1);
        printSymbolTable();
        System.out.println();
        secondPass(input2);
        printMemoryTable();
        System.out.println();
        if (!warnings.equals(""))
            System.out.println(warnings);
        if (!extraUsesError.equals(""))
            System.out.println(extraUsesError);
        System.out.println("*************************************************\n");

            input1.close();
            input2.close();


        textLine.close();



    }

}
