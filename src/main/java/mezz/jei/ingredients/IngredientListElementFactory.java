package mezz.jei.ingredients;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.gui.ingredients.IIngredientListElement;
import mezz.jei.startup.IModIdHelper;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.ProgressManager;

import java.util.Collection;
import java.util.List;

public final class IngredientListElementFactory {
	private IngredientListElementFactory() {
	}

	public static NonNullList<IIngredientListElement> createBaseList(IIngredientRegistry ingredientRegistry, IModIdHelper modIdHelper) {
		NonNullList<IIngredientListElement> ingredientListElements = NonNullList.create();

		for (Class<?> ingredientClass : ingredientRegistry.getRegisteredIngredientClasses()) {
			addToBaseList(ingredientListElements, ingredientRegistry, ingredientClass, modIdHelper);
		}

		ingredientListElements.sort(IngredientListElementComparator.INSTANCE);
		return ingredientListElements;
	}

	public static <V> NonNullList<IIngredientListElement> createList(IIngredientRegistry ingredientRegistry, Class<V> ingredientClass, List<V> ingredients, IModIdHelper modIdHelper) {
		IIngredientHelper<V> ingredientHelper = ingredientRegistry.getIngredientHelper(ingredientClass);
		IIngredientRenderer<V> ingredientRenderer = ingredientRegistry.getIngredientRenderer(ingredientClass);

		NonNullList<IIngredientListElement> list = NonNullList.create();
		for (V ingredient : ingredients) {
			if (ingredient != null) {
				IngredientListElement<V> ingredientListElement = IngredientListElement.create(ingredient, ingredientHelper, ingredientRenderer, modIdHelper);
				if (ingredientListElement != null) {
					list.add(ingredientListElement);
				}
			}
		}
		return list;
	}

	private static <V> void addToBaseList(NonNullList<IIngredientListElement> baseList, IIngredientRegistry ingredientRegistry, Class<V> ingredientClass, IModIdHelper modIdHelper) {
		IIngredientHelper<V> ingredientHelper = ingredientRegistry.getIngredientHelper(ingredientClass);
		IIngredientRenderer<V> ingredientRenderer = ingredientRegistry.getIngredientRenderer(ingredientClass);

		Collection<V> ingredients = ingredientRegistry.getAllIngredients(ingredientClass);
		ProgressManager.ProgressBar progressBar = ProgressManager.push("Registering ingredients: " + ingredientClass.getSimpleName(), ingredients.size());
		for (V ingredient : ingredients) {
			progressBar.step("");
			if (ingredient != null) {
				IngredientListElement<V> ingredientListElement = IngredientListElement.create(ingredient, ingredientHelper, ingredientRenderer, modIdHelper);
				if (ingredientListElement != null) {
					baseList.add(ingredientListElement);
				}
			}
		}
		ProgressManager.pop(progressBar);
	}

}
