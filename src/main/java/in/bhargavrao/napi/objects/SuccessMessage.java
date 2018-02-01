package in.bhargavrao.napi.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhargav.h on 09-Mar-17.
 */
public class SuccessMessage extends  Message{
    List<Item> items;

    public SuccessMessage(){
        items = new ArrayList<Item>();
    }

    public void addItem(Item item){
        this.items.add(item);
    }

    public List<Item> getItems(){
        return items;
    }
}
