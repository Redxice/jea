package helpers;

import java.util.List;

public class DaoHelper {
    /*
        This method is used to prevent the error you get when u use the getSingleResult method from the entityManager.
     */
    public static <type> type getSingleResult(Class type, List<type> list){
        if(list != null && list.size()!=0){
            return list.get(0);
        }else{
            return null;
        }
    }
}
