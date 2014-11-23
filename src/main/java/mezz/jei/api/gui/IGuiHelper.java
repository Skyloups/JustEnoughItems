package mezz.jei.api.gui;

import javax.annotation.Nonnull;

/**
 * Helps with the implementation of GUIs.
 * The instance is in JEIManager.
 */
public interface IGuiHelper {

	@Nonnull
	IGuiItemStack makeGuiItemStack(int xPosition, int yPosition, int padding);

}