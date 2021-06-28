package artifacts.client.render.model.trinket;


import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;

public class SteadfastSpikesModel extends PlayerEntityModel<LivingEntity> {

	public SteadfastSpikesModel() {
		super(0.5F, false);

		ModelPart clawLeft1 = new ModelPart(this, 32, 0);
		ModelPart clawRight1 = new ModelPart(this, 43, 0);
		ModelPart clawLeft2 = new ModelPart(this, 32, 8);
		ModelPart clawRight2 = new ModelPart(this, 43, 8);
		clawLeft1.addCuboid(-1.5F, 9, -7, 1, 3, 5);
		clawRight1.addCuboid(-1.5F, 9, -7, 1, 3, 5);
		clawLeft2.addCuboid(0.5F, 9, -7, 1, 3, 5);
		clawRight2.addCuboid(0.5F, 9, -7, 1, 3, 5);
		leftLeg.addChild(clawLeft1);
		rightLeg.addChild(clawRight1);
		leftLeg.addChild(clawLeft2);
		rightLeg.addChild(clawRight2);

		setVisible(false);
		leftLeg.visible = true;
		rightLeg.visible = true;
		leftPants.visible = true;
		rightPants.visible = true;
	}
}
