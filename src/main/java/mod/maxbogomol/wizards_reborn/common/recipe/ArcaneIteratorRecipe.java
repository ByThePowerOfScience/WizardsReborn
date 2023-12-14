package mod.maxbogomol.wizards_reborn.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mod.maxbogomol.wizards_reborn.WizardsReborn;
import mod.maxbogomol.wizards_reborn.utils.RecipeUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ArcaneIteratorRecipe implements Recipe<Container> {
    public static ResourceLocation TYPE_ID = new ResourceLocation(WizardsReborn.MOD_ID, "arcane_iterator");
    private final ResourceLocation id;
    private final ItemStack output;
    private final Enchantment enchantment;
    private final NonNullList<Ingredient> inputs;
    private final int wissen;
    private final int health;
    private final int experience;
    private final boolean isSaveNBT;

    public ArcaneIteratorRecipe(ResourceLocation id, ItemStack output, Enchantment enchantment, int wissen, int health, int experience, boolean isSaveNBT, NonNullList<Ingredient> inputs) {
        this.id = id;
        this.output = output;
        this.enchantment = enchantment;
        this.inputs = inputs;
        this.wissen = wissen;
        this.health = health;
        this.experience = experience;
        this.isSaveNBT = isSaveNBT;
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        return matches(inputs, inv);
    }

    public static boolean matches(List<Ingredient> inputs, Container inv) {
        List<Ingredient> ingredientsMissing = new ArrayList<>(inputs);

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack input = inv.getItem(i);
            if (input.isEmpty()) {
                break;
            }

            int stackIndex = -1;

            for (int j = 0; j < ingredientsMissing.size(); j++) {
                Ingredient ingr = ingredientsMissing.get(j);
                if (ingr.test(input)) {
                    stackIndex = j;
                    break;
                }
            }

            if (stackIndex != -1) {
                ingredientsMissing.remove(stackIndex);
            } else {
                return false;
            }
        }

        ItemStack stack = inv.getItem(0);
        if (stack.isEmpty()) {
            return false;
        }
        Ingredient ingr = inputs.get(0);
        if (!ingr.test(stack)) {
            return false;
        }

        return ingredientsMissing.isEmpty();
    }

    @Override
    public ItemStack assemble(Container inv, RegistryAccess pRegistryAccess) {
        return output;
    }

    @Nonnull
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputs;
    }

    public int getRecipeWissen() {
        return wissen;
    }

    public int getRecipeHealth() {
        return health;
    }

    public int getRecipeExperience() {
        return experience;
    }

    public Enchantment getRecipeEnchantment() {
        return enchantment;
    }

    public boolean getRecipeIsSaveNBT() {
        return isSaveNBT;
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(WizardsReborn.WISSEN_CRYSTALLIZER_ITEM.get());
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return WizardsReborn.WISSEN_CRYSTALLIZER_SERIALIZER.get();
    }

    public static class ArcaneIteratorRecipeType implements RecipeType<ArcaneIteratorRecipe> {
        @Override
        public String toString() {
            return ArcaneIteratorRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer implements RecipeSerializer<ArcaneIteratorRecipe> {

        @Override
        public ArcaneIteratorRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ItemStack output = ItemStack.EMPTY;
            Enchantment enchantment = null;

            int wissen = GsonHelper.getAsInt(json, "wissen");
            int health = 0;
            int experience = 0;

            boolean isSaveNBT = false;

            if (json.has("output")) {
                output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            }
            if (json.has("enchantment")) {
                enchantment = RecipeUtils.deserializeEnchantment(GsonHelper.getAsJsonObject(json, "enchantment"));
            }

            if (json.has("health")) {
                health = GsonHelper.getAsInt(json, "health");
            }
            if (json.has("experience")) {
                experience = GsonHelper.getAsInt(json, "experience");
            }

            JsonArray ingrs = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.create();
            for (JsonElement e : ingrs) {
                inputs.add(Ingredient.fromJson(e));
            }

            if (json.has("saveNBT")) {
                isSaveNBT = GsonHelper.getAsBoolean(json, "saveNBT");
            }

            return new ArcaneIteratorRecipe(recipeId, output, enchantment, wissen, health, experience, isSaveNBT, inputs);
        }

        @Nullable
        @Override
        public ArcaneIteratorRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            NonNullList<Ingredient> inputs = NonNullList.create();
            int inputsSize = buffer.readInt();
            for (int i = 0; i < inputsSize; i++) {
                inputs.add(Ingredient.fromNetwork(buffer));
            }
            ItemStack output = buffer.readItem();
            Enchantment enchantment = RecipeUtils.enchantmentFromNetwork(buffer);
            int wissen = buffer.readInt();
            int health = buffer.readInt();
            int experience = buffer.readInt();
            boolean isSaveNBT = buffer.readBoolean();
            return new ArcaneIteratorRecipe(recipeId, output, enchantment, wissen, health, experience, isSaveNBT, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ArcaneIteratorRecipe recipe) {
            buffer.writeInt(recipe.getIngredients().size());
            for (Ingredient input : recipe.getIngredients()) {
                input.toNetwork(buffer);
            }
            buffer.writeItemStack(recipe.getResultItem(RegistryAccess.EMPTY), false);
            RecipeUtils.enchantmentToNetwork(recipe.getRecipeEnchantment(), buffer);
            buffer.writeInt(recipe.getRecipeWissen());
            buffer.writeInt(recipe.getRecipeHealth());
            buffer.writeInt(recipe.getRecipeExperience());
            buffer.writeBoolean(recipe.getRecipeIsSaveNBT());
        }
    }

    @Override
    public RecipeType<?> getType(){
        return BuiltInRegistries.RECIPE_TYPE.getOptional(TYPE_ID).get();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output;
    }

    @Override
    public boolean isSpecial(){
        return true;
    }
}
