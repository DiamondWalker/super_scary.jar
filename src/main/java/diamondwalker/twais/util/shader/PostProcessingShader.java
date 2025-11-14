package diamondwalker.twais.util.shader;

import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class PostProcessingShader {
    protected final ResourceLocation location;
    protected final EnumShaderLayer layer;
    protected final boolean removeOnWorldClose;

    private boolean active = false;

    public PostProcessingShader(ResourceLocation location, EnumShaderLayer layer, boolean removeOnWorldClose) {
        this.location = location;
        this.layer = layer;
        this.removeOnWorldClose = removeOnWorldClose;
    }
    
    public void activate() {
        ShaderManager.addEffect(this);
        active = true;
    }
    
    public void deactivate() {
        ShaderManager.removeEffect(this);
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public void setUniform(String uniformName, float x) {
        ShaderManager.setUniform(this, uniformName, (uniform) -> uniform.set(x));
    }

    public void setUniform(String uniformName, float x, float y) {
        ShaderManager.setUniform(this, uniformName, (uniform) -> uniform.set(x, y));
    }

    public void setUniform(String uniformName, float x, float y, float z) {
        ShaderManager.setUniform(this, uniformName, (uniform) -> uniform.set(x, y, z));
    }

    public void setUniform(String uniformName, float x, float y, float z, float w) {
        ShaderManager.setUniform(this, uniformName, (uniform) -> uniform.set(x, y, z, w));
    }

    public void setUniform(String uniformName, int x) {
        ShaderManager.setUniform(this, uniformName, (uniform) -> uniform.set(x));
    }

    public void setUniform(String uniformName, int x, int y) {
        ShaderManager.setUniform(this, uniformName, (uniform) -> uniform.set(x, y));
    }

    public void setUniform(String uniformName, int x, int y, int z) {
        ShaderManager.setUniform(this, uniformName, (uniform) -> uniform.set(x, y, z));
    }

    public void setUniform(String uniformName, int x, int y, int z, int w) {
        ShaderManager.setUniform(this, uniformName, (uniform) -> uniform.set(x, y, z, w));
    }

    public void setUniform(String uniformName, float[] valueArray) {
        ShaderManager.setUniform(this, uniformName, (uniform) -> uniform.set(valueArray));
    }

    public void setUniform(String uniformName, Vector3f vector) {
        ShaderManager.setUniform(this, uniformName, (uniform) -> uniform.set(vector));
    }

    public void setUniform(String uniformName, Vector4f vector) {
        ShaderManager.setUniform(this, uniformName, (uniform) -> uniform.set(vector));
    }

    public void setUniformMat2x2(String uniformName, float m00, float m01, float m10, float m11) {
        ShaderManager.setUniform(this, uniformName, (uniform) -> uniform.setMat2x2(m00, m01, m10, m11));
    }

    public void setUniformMat2x3(String uniformName, float m00, float m01, float m02, float m10, float m11, float m12) {
        ShaderManager.setUniform(this, uniformName, (uniform) -> uniform.setMat2x3(m00, m01, m02, m10, m11, m12));
    }

    public void setUniformMat2x4(String uniformName, float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13) {
        ShaderManager.setUniform(this, uniformName, (uniform) -> uniform.setMat2x4(m00, m01, m02, m03, m10, m11, m12, m13));
    }

    public void setUniformMat3x2(String uniformName, float m00, float m01, float m10, float m11, float m20, float m21) {
        ShaderManager.setUniform(this, uniformName, (uniform) -> uniform.setMat3x2(m00, m01, m10, m11, m20, m21));
    }

    public void setUniformMat3x3(String uniformName, float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22) {
        ShaderManager.setUniform(this, uniformName, (uniform) -> uniform.setMat3x3(m00, m01, m02, m10, m11, m12, m20, m21, m22));
    }

    public void setUniformMat3x4(String uniformName, float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23) {
        ShaderManager.setUniform(this, uniformName, (uniform) -> uniform.setMat3x4(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23));
    }

    public void setUniformMat4x2(String uniformName, float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13) {
        ShaderManager.setUniform(this, uniformName, (uniform) -> uniform.setMat4x2(m00, m01, m02, m03, m10, m11, m12, m13));
    }

    public void setUniformMat4x3(String uniformName, float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23) {
        ShaderManager.setUniform(this, uniformName, (uniform) -> uniform.setMat4x3(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23));
    }

    public void setUniformMat4x4(String uniformName, float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
        ShaderManager.setUniform(this, uniformName, (uniform) -> uniform.setMat4x4(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33));
    }

    public void setUniform(String uniformName, Matrix4f matrix) {
        ShaderManager.setUniform(this, uniformName, (uniform) -> uniform.set(matrix));
    }

    public void setUniform(String uniformName, Matrix3f matrix) {
        ShaderManager.setUniform(this, uniformName, (uniform) -> uniform.set(matrix));
    }
}
