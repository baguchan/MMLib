package cn.mcmod_mmf.mmlib.client.model.bedrock;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.model.geom.PartPose;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Random;

import org.apache.commons.compress.utils.Lists;

@OnlyIn(Dist.CLIENT)
public final class BedrockPart {
    public final ObjectList<BedrockCube> cubes;
    private final List<BedrockPart> children;
    
    public float x;
    public float y;
    public float z;
    public float xRot;
    public float yRot;
    public float zRot;

    public boolean visible;
    public boolean mirror;
    
    public BedrockPart() {
        cubes = new ObjectArrayList<>();
        children = Lists.newArrayList();
        visible = true;
    }

    public void setPos(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int texU, int texV) {
        this.render(poseStack, consumer, texU, texV, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int texU, int texV, float red, float green,
            float blue, float alpha) {
        if (this.visible) {
            if (!this.cubes.isEmpty() || !this.children.isEmpty()) {
                poseStack.pushPose();
                this.translateAndRotate(poseStack);
                this.compile(poseStack.last(), consumer, texU, texV, red, green, blue, alpha);
                
                for (BedrockPart part : this.children) {
                    part.render(poseStack, consumer, texU, texV, red, green, blue, alpha);
                }

                poseStack.popPose();
            }
        }
    }

    public void translateAndRotate(PoseStack poseStack) {
        poseStack.translate(this.x / 16.0F, this.y / 16.0F, this.z / 16.0F);
        if (this.zRot != 0.0F) {
            poseStack.mulPose(Vector3f.ZP.rotation(this.zRot));
        }

        if (this.yRot != 0.0F) {
            poseStack.mulPose(Vector3f.YP.rotation(this.yRot));
        }

        if (this.xRot != 0.0F) {
            poseStack.mulPose(Vector3f.XP.rotation(this.xRot));
        }
     }

    private void compile(PoseStack.Pose pose, VertexConsumer consumer, int texU, int texV, float red, float green,
            float blue, float alpha) {
        for (BedrockCube bedrockCube : this.cubes) {
            bedrockCube.compile(pose, consumer, texU, texV, red, green, blue, alpha);
        }
    }

    public BedrockCube getRandomCube(Random random) {
        return this.cubes.get(random.nextInt(this.cubes.size()));
    }

    public boolean isEmpty() {
        return this.cubes.isEmpty();
    }

    public PartPose storePose() {
        return PartPose.offsetAndRotation(this.x, this.y, this.z, this.xRot, this.yRot, this.zRot);
    }

    public void loadPose(PartPose p_171323_) {
        this.x = p_171323_.x;
        this.y = p_171323_.y;
        this.z = p_171323_.z;
        this.xRot = p_171323_.xRot;
        this.yRot = p_171323_.yRot;
        this.zRot = p_171323_.zRot;
    }

    public void copyFrom(BedrockPart p_104316_) {
        this.xRot = p_104316_.xRot;
        this.yRot = p_104316_.yRot;
        this.zRot = p_104316_.zRot;
        this.x = p_104316_.x;
        this.y = p_104316_.y;
        this.z = p_104316_.z;
    }

    public void addChild(BedrockPart model) {
        this.children.add(model);
    }
    
    public List<BedrockPart> getChildren() {
        return children;
    }

}
