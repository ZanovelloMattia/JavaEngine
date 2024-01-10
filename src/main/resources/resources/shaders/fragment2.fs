#version 410 core

 in vec2 fragTextureCoord;
 in vec3 fragNormal;
 in vec3 fragToCameraVector;
 in vec3 fnLightPos;


out vec4 fragColour;

struct Material {
//vec4 ambient;
//vec4 diffuse;
//vec4 specular;
    float reflectance;
    float reflectancePow;
};



uniform sampler2D textureSampler;
uniform vec3 lightColor;


void main(){
    vec3 nNormal = normalize(fragNormal);
    float diffuse = max(dot(nNormal, fnLightPos), 0.0f);
    float ambient = 0.2f;

    vec3 nfragToCameraVector = normalize(fragToCameraVector);

    vec3 totalDiffuse = vec3(0.6);
    vec3 totalSpecular = vec3(0.0);

    fragColour = texture(textureSampler, fragTextureCoord) * vec4(lightColor, 1.0) *(vec4(diffuse, diffuse,  diffuse, 1.0)/(vec4(diffuse, diffuse,  diffuse, 1.0))) + vec4(0.3f, 0.3, 0.3, 0);// * (vec4(diffuse, diffuse,  diffuse, 1.0) *  ambient);
}