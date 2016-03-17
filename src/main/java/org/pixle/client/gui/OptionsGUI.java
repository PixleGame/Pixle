package org.pixle.client.gui;

import org.lwjgl.opengl.Display;
import org.pixle.client.PixleClient;
import org.pixle.client.config.ClientConfig;
import org.pixle.client.config.ConfigOption;
import org.pixle.client.config.OptionType;
import org.pixle.client.gl.GLStateManager;
import org.pixle.client.gui.component.ButtonComponent;
import org.pixle.client.gui.component.GUIOptionComponent;
import org.pixle.client.gui.component.KeyOptionComponent;
import org.pixle.client.gui.component.SliderOptionComponent;
import org.pixle.client.render.RenderHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionsGUI extends GUI {
    private List<String> names = new ArrayList<>();
    private Map<Field, GUIOptionComponent> options = new HashMap<>();

    @Override
    public void updateComponents() {
        names.clear();
        options.clear();
        int width = Display.getWidth();
        int height = Display.getHeight();
        int y = height / 4;
        int optionX = width / 2 - 100;
        try {
            for (Field field : ClientConfig.class.getDeclaredFields()) {
                ConfigOption annotation = field.getDeclaredAnnotation(ConfigOption.class);
                OptionType type = annotation.type();
                GUIOptionComponent component = null;
                if (type == OptionType.KEY) {
                    component = new KeyOptionComponent(optionX, y, 200, 30, (Integer) field.get(PixleClient.INSTANCE.config));
                } else if (type == OptionType.SLIDER) {
                    component = new SliderOptionComponent(optionX, y, annotation.minValue(), annotation.maxValue(), 200, (Integer) field.get(PixleClient.INSTANCE.config));
                }
                if (component != null) {
                    addComponent(component);
                    options.put(field, component);
                    names.add(annotation.name() + " - ");
                    y += 40;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        addComponent(new ButtonComponent(optionX, (height - (height / 8)) - 20, 200, 40, "Done", button -> {
            PixleClient client = PixleClient.INSTANCE;
            for (Map.Entry<Field, GUIOptionComponent> entry : options.entrySet()) {
                try {
                    entry.getKey().set(PixleClient.INSTANCE.config, entry.getValue().set());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            OptionsGUI.this.close();
            client.openGUI(new MainMenuGUI());
        }));
    }

    @Override
    public void render(int mouseX, int mouseY) {
        int width = Display.getWidth();
        int height = Display.getHeight();
        GLStateManager.setColor(0x0094FF);
        RenderHelper.drawRect(0, 0, width, height);
        RenderHelper.drawCenteredScaledStringWithShadow(width / 2, 40, "Options", 2.0F);

        int y = (height / 4) + 10;

        for (String name : names) {
            RenderHelper.drawScaledStringWithShadow(20, y, name, 1.0F);
            y += 40;
        }

        super.render(mouseX, mouseY);
    }
}
