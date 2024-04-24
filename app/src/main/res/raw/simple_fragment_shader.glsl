
// Set the default precision to medium, don't need as high precision in the fragment shader.
precision mediump float;

uniform sampler2D u_Texture; // uniform variable to keep track of the texture coordinate
// slots to use @ sampling a texture

// This is the color from the vertex shader interpolated across the triangle per fragment.
varying vec4 v_Color;
varying lowp vec2 frag_TexCoord;
varying lowp vec3 frag_Normal;
varying lowp vec3 frag_Position;

struct Light {
    vec3 Color;// R, G, B
    float AmbientIntensity;
    float DiffuseIntensity;
    vec3 Direction;
    float SpecularIntensity;
    float Shininess;
};

uniform Light u_Light;

// The entry point for our fragment shader.
void main()
{
    // Ambient
    lowp vec3 AmbientColor = u_Light.Color * u_Light.AmbientIntensity;

    // Diffuse
    lowp vec3 Normal = normalize(frag_Normal);
    lowp float DiffuseFactor = max(-dot(Normal, u_Light.Direction), 0.0);
    lowp vec3 DiffuseColor = u_Light.Color * u_Light.DiffuseIntensity * DiffuseFactor;
    gl_FragColor = texture2D(u_Texture, frag_TexCoord) * vec4((AmbientColor +
    DiffuseColor), 1.0);

    // Specular
    lowp vec3 Eye = normalize(frag_Position);
    lowp vec3 Reflection = reflect(u_Light.Direction, Normal);
    lowp float SpecularFactor = pow(max(0.0, -dot(Reflection, Eye)), u_Light.Shininess);
    lowp vec3 SpecularColor = u_Light.Color * u_Light.SpecularIntensity * SpecularFactor;
    gl_FragColor = texture2D(u_Texture, frag_TexCoord) * vec4((AmbientColor +
    DiffuseColor + SpecularColor), 1.0);


}
