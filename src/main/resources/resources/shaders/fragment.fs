#version 400 core

in vec2 fragTextureCoord;
in vec3 fragNormal;
in vec3 fragToLightVector[3];
in vec3 fragToCameraVector;


out vec4 fragColour;

struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    float reflectance;
    float reflectancePow;
    int haveTexture;
};

struct SunLight{
    vec3 colour;
    //vec3 pos;
    float intensity;
};


uniform Material material;
uniform SunLight sunlight[3];
uniform sampler2D textureSampler;


void main(){
    vec3 nNormal = normalize(fragNormal);
    vec3 nfragToCameraVector = normalize(fragToCameraVector);

    vec3 totalDiffuse = vec3(0.0);
    vec4 totalSpecular = vec4(0.0);

    float minDiffuse;
    int i;

    for(int i = 0; i < 3; i++){
        if(minDiffuse > sunlight[0].intensity){
            minDiffuse = sunlight[0].intensity;
        }else{
            minDiffuse = minDiffuse;
        }

        vec3 nfragToLightVector = normalize(fragToLightVector[i]);
        float ndotD = max(dot(nNormal, nfragToLightVector), 0.0);
        totalDiffuse = totalDiffuse + ndotD * sunlight[i].colour;
        vec3 lightDirection = -nfragToLightVector;
        vec3 reflectedLightDirection = normalize(reflect(lightDirection, nNormal));
        float specularFactor = max(dot(nfragToCameraVector, reflectedLightDirection), 0.0);
        float dumpedSpecularFactor = pow(specularFactor, material.reflectancePow);
        totalSpecular = totalSpecular + vec4(dumpedSpecularFactor * sunlight[i].colour * material.reflectance, 1.0);// * material.specular / material.specular * material.ambient / material.ambient * material.diffuse / material.diffuse;
    }
    totalDiffuse = max(totalDiffuse, minDiffuse)/3;
    if(material.reflectance == 0){
        if(material.haveTexture == 1){
            fragColour = texture(textureSampler, fragTextureCoord) * vec4(totalDiffuse, 1.0);
        }else {
            fragColour = vec4(totalDiffuse, 1.0) * material.specular * material.ambient * material.diffuse;
        }
    }
    else{
        if(material.haveTexture == 1){
            fragColour = texture(textureSampler, fragTextureCoord) * vec4(totalDiffuse, 1.0) + totalSpecular;
        }else {
            fragColour = vec4(totalDiffuse, 1.0) + totalSpecular;
        }

    }
}