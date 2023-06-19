package cn.dioxide.util;

import org.joml.Matrix4f;

/**
 * @author Dioxide.CN
 * @date 2023/6/19
 * @since 1.0
 */
public class MatrixUtil {

    // 计算仿射矩阵
    public static Matrix4f getMatrix(double rx, double ry, double rz, double scale) {
        Matrix4f affineMatrix = new Matrix4f();
        // Scale
        affineMatrix.scale((float) scale);
        // Rotate
        affineMatrix.rotateX((float) Math.toRadians(rx));
        affineMatrix.rotateY((float) Math.toRadians(ry));
        affineMatrix.rotateZ((float) Math.toRadians(rz));

        return affineMatrix;
    }

}
