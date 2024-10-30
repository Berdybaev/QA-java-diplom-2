package practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

public class IngredientDto {
    String _id;
    String name;
    String type;
    String proteins;
    String fat;
    String carbohydrates;
    String calories;
    String price;
    String image;
    String image_mobile;
    String image_large;
    String __v;
}

