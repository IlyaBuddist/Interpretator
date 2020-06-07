package src.library;

import java.util.HashMap;
import java.util.Map;

public class Functions {
    private static final NumberValue ZERO= new NumberValue(0);
    private static Map<String , Function > functions;
    static {
        functions = new HashMap<>();
        functions.put("sin", new Function() {

            public Value execute(Value... args){
                if (args.length != 1) throw new RuntimeException("One args expected");
                return new NumberValue(Math.sin(args[0].asNumber()));
            }
        });
        functions.put("cos",(Function) (Value... args) -> {
            if (args.length != 1) throw new RuntimeException("One args expected");
            return new NumberValue(Math.cos(args[0].asNumber()));
        });
        functions.put("sqrt",(Function) (Value... args) -> {
            if (args.length != 1) throw new RuntimeException("One args expected");
            return new NumberValue(Math.sqrt(args[0].asNumber()));
        });
        functions.put("ciout" , (Function) (Value... args) -> {
            for (Value arg : args){
                System.out.println(arg.asString());
            }
            return ZERO;
        });
    }
    public static boolean isExists(String key){
        return functions.containsKey(key);
    }
    public static Function get(String key){//получение константы по ключу
        if (!isExists(key)) throw new RuntimeException("Unknown function" + key);
        return functions.get(key);
    }

    public static void set(String key , Function function){
        functions.put(key,function);
    }
}
