package org.pixle.client.gui;

import org.pixle.client.PixleClient;
import org.pixle.client.config.ClientConfig;
import org.pixle.client.config.ConfigOption;
import org.pixle.client.config.OptionType;
import org.pixle.client.gl.GLStateManager;
import org.pixle.client.gui.component.*;
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
    public void updateComponents(RenderResolution renderResolution) {
        names.clear();
        options.clear();
        int width = renderResolution.getWidth();
        int height = renderResolution.getHeight();
        int y = height / 4;
        int optionX = width / 2 - 100;
        ClientConfig config = PixleClient.INSTANCE.config;
        try {
            for (Field field : ClientConfig.class.getDeclaredFields()) {
                ConfigOption annotation = field.getDeclaredAnnotation(ConfigOption.class);
                OptionType type = annotation.type();
                GUIOptionComponent component = null;
                if (type == OptionType.KEY) {
                    component = new KeyOptionComponent(optionX, y, 200, 30, (Integer) field.get(config));
                } else if (type == OptionType.SLIDER) {
                    component = new SliderOptionComponent(optionX, y, annotation.minValue(), annotation.maxValue(), 200, (Integer) field.get(config));
                } else if (type == OptionType.BOOLEAN) {
                    component = new BooleanOptionComponent(optionX, y, 200, 30, (Boolean) field.get(config));
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
                    entry.getKey().set(config, entry.getValue().set());
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
        RenderResolution renderResolution = PixleClient.INSTANCE.getRenderResolution();
        int width = renderResolution.getWidth();
        int height = renderResolution.getHeight();
        GLStateManager.setColor(0x0094FF);
        RenderHelper.drawRect(0, 0, width, height);
        RenderHelper.drawCenteredScaledStringWithShadow(width / 2, 60, "Options", 4.0F);

        int y = (height / 4) + 10;

        for (String name : names) {
            RenderHelper.drawScaledStringWithShadow(20, y, name, 2.0F);
            y += 40;
        }

        super.render(mouseX, mouseY);
    }
}
