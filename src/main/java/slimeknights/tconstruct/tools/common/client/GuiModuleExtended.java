package slimeknights.tconstruct.tools.common.client;

import net.minecraft.inventory.Container;
import slimeknights.mantle.client.gui.GuiModule;
import slimeknights.mantle.client.gui.GuiMultiModule;

public abstract class GuiModuleExtended extends GuiModule {

	public GuiModuleExtended(GuiMultiModule parent, Container container, boolean right, boolean bottom) {
		super(parent, container, right, bottom);
	}
	
	public void setGuiLeft(int i) {
		this.guiLeft=i;
	}
	
	public void setGuiTop(int i) {
		this.guiTop=i;
	}
	
	public void setXSize(int i) {
		this.xSize=i;
	}
	
	public void setYSize(int i) {
		this.ySize=i;
	}

}
