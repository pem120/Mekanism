package mekanism.common.config;

import mekanism.common.config.value.CachedLongValue;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.neoforge.common.ModConfigSpec;

public class StorageConfig extends BaseMekanismConfig {

    private final ModConfigSpec configSpec;

    public final CachedLongValue enrichmentChamber;
    public final CachedLongValue osmiumCompressor;
    public final CachedLongValue combiner;
    public final CachedLongValue crusher;
    public final CachedLongValue metallurgicInfuser;
    public final CachedLongValue purificationChamber;
    public final CachedLongValue energizedSmelter;
    public final CachedLongValue digitalMiner;
    public final CachedLongValue electricPump;
    public final CachedLongValue chargePad;
    public final CachedLongValue rotaryCondensentrator;
    public final CachedLongValue chemicalOxidizer;
    public final CachedLongValue chemicalInfuser;
    public final CachedLongValue chemicalInjectionChamber;
    public final CachedLongValue electrolyticSeparator;
    public final CachedLongValue precisionSawmill;
    public final CachedLongValue chemicalDissolutionChamber;
    public final CachedLongValue chemicalWasher;
    public final CachedLongValue chemicalCrystallizer;
    public final CachedLongValue seismicVibrator;
    public final CachedLongValue pressurizedReactionBase;
    public final CachedLongValue fluidicPlenisher;
    public final CachedLongValue laser;
    public final CachedLongValue laserAmplifier;
    public final CachedLongValue laserTractorBeam;
    public final CachedLongValue formulaicAssemblicator;
    public final CachedLongValue teleporter;
    public final CachedLongValue modificationStation;
    public final CachedLongValue isotopicCentrifuge;
    public final CachedLongValue nutritionalLiquifier;
    public final CachedLongValue antiprotonicNucleosynthesizer;
    public final CachedLongValue pigmentExtractor;
    public final CachedLongValue pigmentMixer;
    public final CachedLongValue paintingMachine;
    public final CachedLongValue spsPort;
    public final CachedLongValue dimensionalStabilizer;

    StorageConfig() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.comment("Machine Energy Storage Config. This config is synced from server to client.").push("storage");

        enrichmentChamber = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "enrichmentChamber",
              20_000L, 1);
        osmiumCompressor = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "osmiumCompressor",
              80_000L, 1);
        combiner = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "combiner",
              40_000L, 1);
        crusher = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "crusher",
              20_000L, 1);
        metallurgicInfuser = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "metallurgicInfuser",
              20_000L, 1);
        purificationChamber = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "purificationChamber",
              80_000L, 1);
        energizedSmelter = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "energizedSmelter",
              20_000L, 1);
        digitalMiner = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "digitalMiner",
              50_000L, 1);
        electricPump = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "electricPump",
              40_000L, 1);
        chargePad = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "chargePad", 2_048_000L, 1);
        rotaryCondensentrator = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "rotaryCondensentrator",
              20_000L, 1);
        chemicalOxidizer = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "chemicalOxidizer",
              80_000L, 1);
        chemicalInfuser = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "chemicalInfuser",
              80_000L, 1);
        chemicalInjectionChamber = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "chemicalInjectionChamber",
              160_000L, 1);
        electrolyticSeparator = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "electrolyticSeparator",
              160_000L, 1);
        precisionSawmill = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "precisionSawmill",
              20_000L, 1);
        chemicalDissolutionChamber = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "chemicalDissolutionChamber",
              160_000L, 1);
        chemicalWasher = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "chemicalWasher",
              80_000L, 1);
        chemicalCrystallizer = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "chemicalCrystallizer",
              160_000L, 1);
        seismicVibrator = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "seismicVibrator",
              20_000L, 1);
        pressurizedReactionBase = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "pressurizedReactionBase",
              2_000L, 1);
        fluidicPlenisher = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "fluidicPlenisher",
              40_000L, 1);
        laser = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "laser",
              2_000_000L, 1);
        laserAmplifier = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "laserAmplifier",
              5_000_000_000L, 1);
        laserTractorBeam = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "laserTractorBeam",
              5_000_000_000L, 1);
        formulaicAssemblicator = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "formulaicAssemblicator",
              40_000L, 1);
        teleporter = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "teleporter",
              5_000_000L, 1);
        modificationStation = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "modificationStation",
              40_000L, 1);
        isotopicCentrifuge = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "isotopicCentrifuge",
              80_000L, 1);
        nutritionalLiquifier = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "nutritionalLiquifier",
              40_000L, 1);
        antiprotonicNucleosynthesizer = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules). Also defines max process rate.", "antiprotonicNucleosynthesizer",
              1_000_000_000L, 1);
        pigmentExtractor = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "pigmentExtractor",
              40_000L, 1);
        pigmentMixer = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "pigmentMixer",
              80_000L, 1);
        paintingMachine = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "paintingMachine",
              40_000L, 1);
        spsPort = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules). Also defines max output rate.", "spsPort",
              1_000_000_000L, 1);
        dimensionalStabilizer = CachedLongValue.defineUnsigned(this, builder, "Base energy storage (Joules).", "dimensionalStabilizer",
              40_000L, 1);

        builder.pop();
        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "machine-storage";
    }

    @Override
    public ModConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public Type getConfigType() {
        return Type.SERVER;
    }

    @Override
    public boolean addToContainer() {
        return false;
    }
}