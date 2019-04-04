package dao;

import java.util.List;

public class DoaHelper {
    public static <type> type getSingleResult(Class type, List<type> list){
        if(list != null && list.size()!=0){
            return list.get(0);
        }else{
            return null;
        }
    }
}
