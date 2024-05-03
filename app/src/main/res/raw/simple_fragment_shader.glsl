
// Set the default precision to medium, don't need as high precision in the fragment shader.
precision mediump float;

uniform sampler2D u_Texture; // uniform variable to keep track of the texture coordinate
// slots to use @ sampling a texture

// This is the color from the vertex shader interpolated across the triangle per fragment.
varying vec4 v_Color;
varying lowp vec2 frag_TexCoord;
varying lowp vec3 frag_Normal;
varying lowp vec3 frag_Position;

struct Material {
    vec3 AmbientColor;      // Ambient Color (Ka)
    vec3 DiffuseColor;      // Diffuse Color (Kd)
    vec3 SpecularColor;     // Specular Color (Ks)
    float Shininess;        // Shininess (Ns)
    float Dissolve;
};

struct Light {
    vec3 Direction;
};

uniform Material u_Material;
uniform Light u_Light;

// The entry point for our fragment shader.
void main()
{

    // Ambient
    lowp vec3 AmbientColor = u_Material.AmbientColor;

    // Diffuse
    lowp vec3 Normal = normalize(frag_Normal);
    lowp float DiffuseFactor = max(-dot(Normal, u_Light.Direction), 0.0); // Assuming light direction is towards the negative Z-axis
    lowp vec3 DiffuseColor = u_Material.DiffuseColor * DiffuseFactor;

    // Specular
    lowp vec3 Eye = normalize(frag_Position);
    lowp vec3 Reflection = reflect(u_Light.Direction, Normal); // Assuming light direction is towards the negative Z-axis
    lowp float SpecularFactor = pow(max(0.0, -dot(Reflection, Eye)), u_Material.Shininess);
    lowp vec3 SpecularColor = u_Material.SpecularColor * SpecularFactor;

    // Apply lighting model
    lowp vec4 FinalColor = texture2D(u_Texture, frag_TexCoord) * vec4((AmbientColor + DiffuseColor + SpecularColor), 1.0);

    // Apply transparency
    if (u_Material.Dissolve < 1.0) {
        // Adjust alpha based on dissolve factor
        FinalColor.a *= u_Material.Dissolve;
    }

    gl_FragColor = FinalColor;

}
