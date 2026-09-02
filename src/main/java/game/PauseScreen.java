package game;

import bagel.*;
import java.util.Properties;

public class PauseScreen {
    private final Font titleFont;
    private final Font defaultFont;
    private final String titleText;
    private final double titleY;
    private final String[] controlsLines;
    private final double controlsStartY, rowGap;

    public PauseScreen() {
        GameConfig config = GameConfig.getInstance();
        String fontPath = config.getProperty("text.font");
        int defaultSize = Integer.parseInt(config.getProperty("text.size"));

        titleText = config.getProperty("pausedTitle.text");
        titleY = Double.parseDouble(config.getProperty("pausedTitle.posY"));
        int titleSize = Integer.parseInt(
                config.getProperty(
                        "pausedTitle.size",
                        String.valueOf(defaultSize)
                )
        );
        titleFont = new Font(fontPath, titleSize);

        controlsLines = config.getProperty("controlsList.text").split(",");
        controlsStartY = Double.parseDouble(
                config.getProperty("controlsList.startPosY")
        );
        rowGap = Double.parseDouble(config.getProperty("controlsList.rowGap"));

        defaultFont = new Font(fontPath, defaultSize);
    }

    public void render(
            double screenWidth,
            int timescale,
            bagel.util.Colour textColour
    ) {
        DrawOptions opts = new DrawOptions().setBlendColour(textColour);

        // Centre the title horizontally
        double titleX = (screenWidth - titleFont.getWidth(titleText)) / 2;
        titleFont.drawString(titleText, titleX, titleY);

        // Draw each control hint line, centred
        for (int i = 0; i < controlsLines.length; i++) {
            String line = controlsLines[i].trim();
            double lineX = (screenWidth - defaultFont.getWidth(line)) / 2;
            double lineY = controlsStartY + (i * rowGap);
            defaultFont.drawString(line, lineX, lineY);
        }
    }
}
