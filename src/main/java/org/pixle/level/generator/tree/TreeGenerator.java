package org.pixle.level.generator.tree;

import org.pixle.level.Level;
import org.pixle.level.generator.ImageStructureGenerator;

import java.io.IOException;
import java.util.Random;

public class TreeGenerator {
    private static final String[] treeTypes = new String[]{"tree_1", "tree_2"};

    private static String getTreeLocation(String treeType) {
        return "textures/generation/trees/" + treeType + ".png";
    }

    public static void generateTree(Level level, int x, int y, Random rand) {
        String treeType = treeTypes[rand.nextInt(treeTypes.length)];
        try {
            ImageStructureGenerator.generateFromImage(level, x, y - 8, getTreeLocation(treeType), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
