package net.ilexiconn.pixle.level.generator.tree;

import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.level.generator.ImageStructureGenerator;

import java.io.IOException;
import java.util.Random;

public class TreeGenerator {
    private static final String[] treeTypes = new String[] { "tree_1" };

    private static String getTreeLocation(String treeType) {
        return "textures/generation/trees/" + treeType + ".png";
    }

    public static void generateTree(Level level, int x, int y, Random rand) {
        String treeType = treeTypes[rand.nextInt(treeTypes.length)];
        try {
            ImageStructureGenerator.generateFromImage(level, x, y - 8, getTreeLocation(treeType));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
