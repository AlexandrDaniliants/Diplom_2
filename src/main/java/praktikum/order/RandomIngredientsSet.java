package praktikum.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static praktikum.constants.OrderConstants.INGREDIENTS_COUNT;

public class RandomIngredientsSet {

    private List<String> data = new ArrayList<>(); // Используем интерфейс List
    private Random random = new Random();

    public List<String> createRandomOrder(Ingredients ingredients) {
        data.clear(); // Очищаем список перед созданием нового заказа
        int n = 1 + random.nextInt(INGREDIENTS_COUNT - 1);
        for (int i = 0; i < n; i++) {
            int ingredientIndex = random.nextInt(INGREDIENTS_COUNT);
            data.add(ingredients.getData().get(ingredientIndex).get_id());
        }
        return data;
    }
}
