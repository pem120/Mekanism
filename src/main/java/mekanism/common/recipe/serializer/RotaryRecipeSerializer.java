package mekanism.common.recipe.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mekanism.api.JsonConstants;
import mekanism.api.SerializerHelper;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.RotaryRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.Mekanism;
import mekanism.common.recipe.ingredient.creator.FluidStackIngredientCreator;
import mekanism.common.recipe.ingredient.creator.GasStackIngredientCreator;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class RotaryRecipeSerializer<RECIPE extends RotaryRecipe> implements RecipeSerializer<RECIPE> {

    private final  RecordCodecBuilder<RECIPE, FluidStackIngredient> FLUID_INPUT_FIELD = RecordCodecBuilder.of(RotaryRecipe::getFluidInput, JsonConstants.FLUID_INPUT, FluidStackIngredientCreator.INSTANCE.codec());
    private final  RecordCodecBuilder<RECIPE, FluidStack> FLUID_OUTPUT_FIELD = RecordCodecBuilder.of(r->r.getFluidOutput(GasStack.EMPTY), JsonConstants.FLUID_OUTPUT, SerializerHelper.FLUIDSTACK_CODEC);
    private final RecordCodecBuilder<RECIPE, GasStackIngredient> GAS_INPUT_FIELD = RecordCodecBuilder.of(RotaryRecipe::getGasInput, JsonConstants.GAS_INPUT, GasStackIngredientCreator.INSTANCE.codec());
    private final RecordCodecBuilder<RECIPE, GasStack> GAS_OUTPUT_FIELD = RecordCodecBuilder.of(rotaryRecipe -> rotaryRecipe.getGasOutput(FluidStack.EMPTY), JsonConstants.GAS_INPUT, GasStack.CODEC);

    private Codec<RECIPE> bothWaysCodec() {
        return RecordCodecBuilder.create(i -> i.group(
              FLUID_INPUT_FIELD,
              GAS_INPUT_FIELD,
              GAS_OUTPUT_FIELD,
              FLUID_OUTPUT_FIELD
        ).apply(i, this.factory::create));
    }

    private Codec<RECIPE> fluidToGasCodec() {
        return RecordCodecBuilder.create(i -> i.group(
              FLUID_INPUT_FIELD,
              GAS_OUTPUT_FIELD
        ).apply(i, this.factory::create));
    }

    private Codec<RECIPE> gasToFluidCodec() {
        return RecordCodecBuilder.create(i -> i.group(
              GAS_INPUT_FIELD,
              FLUID_OUTPUT_FIELD
        ).apply(i, this.factory::create));
    }

    private final IFactory<RECIPE> factory;
    private final Lazy<Codec<RECIPE>> codec;

    public RotaryRecipeSerializer(IFactory<RECIPE> factory) {
        this.factory = factory;
        this.codec = Lazy.of(this::makeCodec);
    }

    private Codec<RECIPE> makeCodec() {
        return ExtraCodecs.withAlternative(bothWaysCodec(), ExtraCodecs.withAlternative(fluidToGasCodec(), gasToFluidCodec()));
    }

    public RECIPE fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        FluidStackIngredient fluidInputIngredient = null;
        GasStackIngredient gasInputIngredient = null;
        GasStack gasOutput = null;
        FluidStack fluidOutput = null;
        boolean hasFluidToGas = false;
        boolean hasGasToFluid = false;
        if (json.has(JsonConstants.FLUID_INPUT) || json.has(JsonConstants.GAS_OUTPUT)) {
            JsonElement fluidInput = GsonHelper.isArrayNode(json, JsonConstants.FLUID_INPUT) ? GsonHelper.getAsJsonArray(json, JsonConstants.FLUID_INPUT) :
                                     GsonHelper.getAsJsonObject(json, JsonConstants.FLUID_INPUT);
            fluidInputIngredient = IngredientCreatorAccess.fluid().deserialize(fluidInput);
            gasOutput = SerializerHelper.getGasStack(json, JsonConstants.GAS_OUTPUT);
            hasFluidToGas = true;
            if (gasOutput.isEmpty()) {
                throw new JsonSyntaxException("Rotary recipe gas output cannot be empty if it is defined.");
            }
        }
        if (json.has(JsonConstants.GAS_INPUT) || json.has(JsonConstants.FLUID_OUTPUT)) {
            JsonElement gasInput = GsonHelper.isArrayNode(json, JsonConstants.GAS_INPUT) ? GsonHelper.getAsJsonArray(json, JsonConstants.GAS_INPUT) :
                                   GsonHelper.getAsJsonObject(json, JsonConstants.GAS_INPUT);
            gasInputIngredient = IngredientCreatorAccess.gas().deserialize(gasInput);
            fluidOutput = SerializerHelper.getFluidStack(json, JsonConstants.FLUID_OUTPUT);
            hasGasToFluid = true;
            if (fluidOutput.isEmpty()) {
                throw new JsonSyntaxException("Rotary recipe fluid output cannot be empty if it is defined.");
            }
        }
        if (hasFluidToGas && hasGasToFluid) {
            return this.factory.create(fluidInputIngredient, gasInputIngredient, gasOutput, fluidOutput);
        } else if (hasFluidToGas) {
            return this.factory.create(fluidInputIngredient, gasOutput);
        } else if (hasGasToFluid) {
            return this.factory.create(gasInputIngredient, fluidOutput);
        }
        throw new JsonSyntaxException("Rotary recipes require at least a gas to fluid or fluid to gas conversion.");
    }

    @Override
    @NotNull
    public Codec<RECIPE> codec() {
        return this.codec.get();
    }

    @Override
    public RECIPE fromNetwork(@NotNull FriendlyByteBuf buffer) {
        try {
            FluidStackIngredient fluidInputIngredient = null;
            GasStackIngredient gasInputIngredient = null;
            GasStack gasOutput = null;
            FluidStack fluidOutput = null;
            boolean hasFluidToGas = buffer.readBoolean();
            if (hasFluidToGas) {
                fluidInputIngredient = IngredientCreatorAccess.fluid().read(buffer);
                gasOutput = GasStack.readFromPacket(buffer);
            }
            boolean hasGasToFluid = buffer.readBoolean();
            if (hasGasToFluid) {
                gasInputIngredient = IngredientCreatorAccess.gas().read(buffer);
                fluidOutput = FluidStack.readFromPacket(buffer);
            }
            if (hasFluidToGas && hasGasToFluid) {
                return this.factory.create(fluidInputIngredient, gasInputIngredient, gasOutput, fluidOutput);
            } else if (hasFluidToGas) {
                return this.factory.create(fluidInputIngredient, gasOutput);
            } else if (hasGasToFluid) {
                return this.factory.create(gasInputIngredient, fluidOutput);
            }
            //Should never happen, but if we somehow get here log it
            Mekanism.logger.error("Error reading rotary recipe from packet. A recipe got sent with no conversion in either direction.");
            return null;
        } catch (Exception e) {
            Mekanism.logger.error("Error reading rotary recipe from packet.", e);
            throw e;
        }
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull RECIPE recipe) {
        try {
            recipe.write(buffer);
        } catch (Exception e) {
            Mekanism.logger.error("Error writing rotary recipe to packet.", e);
            throw e;
        }
    }

    public interface IFactory<RECIPE extends RotaryRecipe> {

        RECIPE create(FluidStackIngredient fluidInput, GasStack gasOutput);

        RECIPE create(GasStackIngredient gasInput, FluidStack fluidOutput);

        RECIPE create(FluidStackIngredient fluidInput, GasStackIngredient gasInput, GasStack gasOutput, FluidStack fluidOutput);
    }
}