package com.github.standobyte.jojo.client.model.entity.stand;

import com.github.standobyte.jojo.entity.stand.stands.MagiciansRedEntity;
import com.github.standobyte.jojo.util.MathUtil;

import net.minecraft.client.renderer.model.ModelRenderer;

// Made with Blockbench 3.9.2


public class MagiciansRedModel extends HumanoidStandModel<MagiciansRedEntity> {
    private ModelRenderer headLeft;
    private ModelRenderer headRight;
    private ModelRenderer beakUpper;
    private ModelRenderer beakUpperLeft;
    private ModelRenderer beakUpperRight;
    private ModelRenderer beakUpper2;
    private ModelRenderer beakUpper3;
    private ModelRenderer beakLower;
    private ModelRenderer beakLowerLeft;
    private ModelRenderer beakLowerRight;
    private ModelRenderer feather;
    private ModelRenderer feather2;
    
    public MagiciansRedModel() {
        this(64, 64);
    }
    
    public MagiciansRedModel(int textureWidth, int textureHeight) {
        super(textureWidth, textureHeight);
    }
    
    @Override
    protected void addBaseBoxes() {
        ModelRenderer actualHead = head;
        this.head = new ModelRenderer(this);
        super.addBaseBoxes();
        this.head = actualHead;
        head.texOffs(24, 0).addBox(-3.5F, -7.0F, -4.0F, 7.0F, 7.0F, 8.0F, 0.0F, false);
        head.texOffs(0, 16).addBox(-4.1F, -7.4F, -4.4F, 1.0F, 7.0F, 2.0F, -0.4F, false);
        head.texOffs(6, 16).addBox(-4.1F, -1.6F, -4.4F, 1.0F, 2.0F, 2.0F, -0.4F, false);
        head.texOffs(0, 16).addBox(3.1F, -7.4F, -4.4F, 1.0F, 7.0F, 2.0F, -0.4F, true);
        head.texOffs(6, 16).addBox(3.1F, -1.6F, -4.4F, 1.0F, 2.0F, 2.0F, -0.4F, true);
        head.texOffs(23, 20).addBox(-2.5F, -1.0F, -4.5F, 5.0F, 1.0F, 2.0F, 0.25F, false);

        headLeft = new ModelRenderer(this);
        headLeft.setPos(3.7F, -3.5F, -2.8F);
        head.addChild(headLeft);
        setRotationAngle(headLeft, 0.0F, -0.0478F, 0.0F);
        headLeft.texOffs(1, 20).addBox(-0.6F, -3.9F, -0.4F, 1.0F, 3.0F, 5.0F, -0.4F, true);
        headLeft.texOffs(8, 16).addBox(-0.6F, -0.5F, -0.4F, 1.0F, 2.0F, 5.0F, -0.4F, true);
        headLeft.texOffs(8, 16).addBox(-0.6F, 1.9F, -0.4F, 1.0F, 2.0F, 5.0F, -0.4F, true);

        headRight = new ModelRenderer(this);
        headRight.setPos(-3.7F, -3.5F, -2.8F);
        head.addChild(headRight);
        setRotationAngle(headRight, 0.0F, 0.0478F, 0.0F);
        headRight.texOffs(1, 20).addBox(-0.4F, -3.9F, -0.4F, 1.0F, 3.0F, 5.0F, -0.4F, false);
        headRight.texOffs(8, 16).addBox(-0.4F, -0.5F, -0.4F, 1.0F, 2.0F, 5.0F, -0.4F, false);
        headRight.texOffs(8, 16).addBox(-0.4F, 1.9F, -0.4F, 1.0F, 2.0F, 5.0F, -0.4F, false);

        beakUpper = new ModelRenderer(this);
        beakUpper.setPos(0.0F, -1.0F, -4.0F);
        head.addChild(beakUpper);
        setRotationAngle(beakUpper, 0.1745F, 0.0F, 0.0F);
        beakUpper.texOffs(46, 16).addBox(-1.0F, -1.0F, -5.5F, 2.0F, 1.0F, 7.0F, 0.25F, false);
        beakUpper.texOffs(54, 24).addBox(-1.5F, -1.0F, -0.5F, 3.0F, 1.0F, 2.0F, 0.25F, false);

        beakUpperLeft = new ModelRenderer(this);
        beakUpperLeft.setPos(1.25F, -0.5F, -5.75F);
        beakUpper.addChild(beakUpperLeft);
        setRotationAngle(beakUpperLeft, 0.0F, 0.2618F, 0.0F);
        beakUpperLeft.texOffs(30, 16).addBox(-1.25F, -0.5F, 0.25F, 1.0F, 1.0F, 7.0F, 0.25F, true);

        beakUpperRight = new ModelRenderer(this);
        beakUpperRight.setPos(-1.25F, -0.5F, -5.75F);
        beakUpper.addChild(beakUpperRight);
        setRotationAngle(beakUpperRight, 0.0F, -0.2618F, 0.0F);
        beakUpperRight.texOffs(30, 16).addBox(0.25F, -0.5F, 0.25F, 1.0F, 1.0F, 7.0F, 0.25F, false);

        beakUpper2 = new ModelRenderer(this);
        beakUpper2.setPos(0.0F, -1.25F, -5.75F);
        beakUpper.addChild(beakUpper2);
        setRotationAngle(beakUpper2, 0.7854F, 0.0F, 0.0F);
        beakUpper2.texOffs(41, 20).addBox(-0.5F, 0.25F, 0.25F, 1.0F, 1.0F, 1.0F, 0.25F, false);

        beakUpper3 = new ModelRenderer(this);
        beakUpper3.setPos(0.0F, 0.0F, 1.5F);
        beakUpper2.addChild(beakUpper3);
        setRotationAngle(beakUpper3, -0.7854F, 0.0F, 0.0F);
        beakUpper3.texOffs(39, 16).addBox(-0.5F, 0.25F, 0.25F, 1.0F, 1.0F, 6.0F, 0.25F, false);

        beakLower = new ModelRenderer(this);
        beakLower.setPos(0.0F, -0.75F, -4.75F);
        head.addChild(beakLower);
        beakLower.texOffs(36, 24).addBox(-1.0F, 0.0F, -4.75F, 2.0F, 1.0F, 6.0F, 0.0F, false);
        beakLower.texOffs(36, 31).addBox(-1.5F, 0.0F, -1.75F, 3.0F, 1.0F, 3.0F, 0.0F, false);
        beakLower.texOffs(23, 24).addBox(-1.0F, -0.3F, -3.75F, 2.0F, 1.0F, 4.0F, 0.0F, false);

        beakLowerLeft = new ModelRenderer(this);
        beakLowerLeft.setPos(1.0F, 0.5F, -4.75F);
        beakLower.addChild(beakLowerLeft);
        setRotationAngle(beakLowerLeft, 0.0F, 0.2618F, 0.0F);
        beakLowerLeft.texOffs(30, 24).addBox(-1.0F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, true);

        beakLowerRight = new ModelRenderer(this);
        beakLowerRight.setPos(-1.0F, 0.5F, -4.75F);
        beakLower.addChild(beakLowerRight);
        setRotationAngle(beakLowerRight, 0.0F, -0.2618F, 0.0F);
        beakLowerRight.texOffs(30, 24).addBox(0.0F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);

        feather = new ModelRenderer(this);
        feather.setPos(0.0F, -6.5F, 4.5F);
        head.addChild(feather);
        setRotationAngle(feather, 0.3927F, 0.0F, 0.0F);
        feather.texOffs(48, 9).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);
        feather.texOffs(58, 13).addBox(-1.0F, 0.5F, 4.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        feather2 = new ModelRenderer(this);
        feather2.setPos(0.0F, -1.0F, -2.5F);
        feather.addChild(feather2);
        setRotationAngle(feather2, 0.3491F, 0.0F, 0.0F);
        feather2.texOffs(48, 1).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);
        feather2.texOffs(58, 5).addBox(-1.0F, 0.5F, 5.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        
        torso.texOffs(48, 34).addBox(-3.5F, 1.1F, -2.0F, 7.0F, 3.0F, 1.0F, 0.4F, false);
        torso.texOffs(52, 27).addBox(-2.5F, 4.0F, -2.3F, 5.0F, 6.0F, 1.0F, 0.0F, false);
    }
    
    @Override
    protected int getSummonPosesCount() {
        return 2;
    }
    
    @Override
    protected void summonPose(float animationFactor, int poseVariant) {
        switch (poseVariant) {
        case 0:
            setSummonPoseRotationAngle(head, 0.0F, 0.1309F, 0.0F, animationFactor);
            if (beakUpper != null) {
                setSummonPoseRotationAngle(beakUpper, -0.3491F, 0.0F, 0.0F, animationFactor);
            }
            if (beakLower != null) {
                setSummonPoseRotationAngle(beakLower, 0.5236F, 0.0F, 0.0F, animationFactor);
            }
            setSummonPoseRotationAngle(body, 0.0F, -0.3927F, 0.0F, animationFactor);
            setSummonPoseRotationAngle(leftArm, 0.0F, 0.0F, -2.3562F, animationFactor);
            setSummonPoseRotationAngle(rightArm, 0.0F, 0.0F, 2.3562F, animationFactor);
            setSummonPoseRotationAngle(leftLeg, 0.1745F, -0.7854F, 0.0F, animationFactor);
            setSummonPoseRotationAngle(rightLeg, -1.5708F, -0.7854F, 0.0F, animationFactor);
            setSummonPoseRotationAngle(rightLowerLeg, 2.3562F, 0.0F, 0.0F, animationFactor);
            break;
        case 1:
            setSummonPoseRotationAngle(head, -0.7854F, 0.0F, 0.0F, animationFactor);
            if (beakUpper != null) {
                setSummonPoseRotationAngle(beakUpper, -0.3491F, 0.0F, 0.0F, animationFactor);
            }
            if (beakLower != null) {
                setSummonPoseRotationAngle(beakLower, 0.5236F, 0.0F, 0.0F, animationFactor);
            }
            setSummonPoseRotationAngle(leftArm, 0.0F, 2.3562F, -2.8798F, animationFactor);
            setSummonPoseRotationAngle(leftForeArm, 0.0F, 0.0F, 2.3562F, animationFactor);
            setSummonPoseRotationAngle(rightArm, 0.0F, -2.3562F, 2.8798F, animationFactor);
            setSummonPoseRotationAngle(rightForeArm, 0.0F, 0.0F, -2.3562F, animationFactor);
            setSummonPoseRotationAngle(leftLeg, 0.2182F, 0.0F, 0.1309F, animationFactor);
            setSummonPoseRotationAngle(rightLeg, 0.5236F, 0.0F, -0.1309F, animationFactor);
            break;
        }
    }
    
    @Override
    protected void rangedAttackPose(MagiciansRedEntity entity, float walkAnimPos, float walkAnimSpeed, float ticks, float yRotationOffset, float xRotation) {
        entity.setYBodyRot(entity.yRot);
        leftArm.setPos(4.0F, -10.0F, 0.0F);
        rightArm.setPos(-4.0F, -10.0F, 0.0F);
        setRotationAngle(head, -0.3927F + xRotation * MathUtil.DEG_TO_RAD, 0.0F, 0.0F);
        if (beakUpper != null) {
            setRotationAngle(beakUpper, -0.3491F, 0.0F, 0.0F);
        }
        if (beakLower != null) {
            setRotationAngle(beakLower, 0.5236F, 0.0F, 0.0F);
        }
        setRotationAngle(leftArm, -1.5708F, 1.0472F, 0.2182F);
        setRotationAngle(leftForeArm, -0.2618F, 0.0F, 0.5236F);
        setRotationAngle(rightArm, -1.3963F, -0.9163F, -0.1745F);
        setRotationAngle(rightForeArm, -0.5236F, 0.0F, -0.6981F);
        setRotationAngle(leftLeg, -0.3927F, 0.0F, 0.0873F);
        setRotationAngle(leftLowerLeg, 0.7854F, 0.0F, 0.0F);
        setRotationAngle(rightLeg, 0.2618F, 0.0F, -0.0873F);
    }
    
    @Override
    public void setupAnim(MagiciansRedEntity entity, float walkAnimPos, float walkAnimSpeed, float ticks, float yRotationOffset, float xRotation) {
        leftArm.setPos(6.0F, -10.0F, 0.0F);
        rightArm.setPos(-6.0F, -10.0F, 0.0F);
        super.setupAnim(entity, walkAnimPos, walkAnimSpeed, ticks, yRotationOffset, xRotation);
    }
    
    @Override
    protected void resetPose() {
        super.resetPose();
        if (beakUpper != null) {
            beakUpper.xRot = 0.1745F;
        }
        if (beakLower != null) {
            beakLower.xRot = 0;
        }
    }
    
    @Override
    protected void specialAbilityPose(MagiciansRedEntity entity, float walkAnimPos, float walkAnimSpeed, float ticks, float yRotationOffset, float xRotation) {
        super.specialAbilityPose(entity, walkAnimPos, walkAnimSpeed, ticks, yRotationOffset, xRotation);
        entity.setYBodyRot(entity.yRot);
        leftArm.xRot = -1.4708F;
        rightArm.xRot = -1.6708F;
        leftArm.yRot = 0.4712F;
        rightArm.yRot = -leftArm.yRot;
    }
}