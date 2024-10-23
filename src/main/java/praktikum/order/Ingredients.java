package praktikum.order;

import java.util.ArrayList;

public class Ingredients {

    private boolean success;
    private ArrayList<IngredientData> data;

    public Ingredients() {
    }

    public Ingredients(boolean success, ArrayList<IngredientData> data) {
        this.success = success;
        this.data = data;
    }

    public ArrayList<IngredientData> getData() {
        return data;
    }

    public void setData(ArrayList<IngredientData> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
