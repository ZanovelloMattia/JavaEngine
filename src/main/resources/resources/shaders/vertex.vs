#version 410 core

in vec3 position;
in vec2 textureCoord;
in vec3 normal;

out vec2 fragTextureCoord;
out vec3 fragNormal;
out vec3 fragToLightVector[3];
out vec3 fragToCameraVector;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition[3];

void main(){
    vec4 worldPos = transformationMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPos;

    fragNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;

    for(int i = 0; i < 3; i++){
        fragToLightVector[i] = lightPosition[i] - worldPos.xyz;
    }
    fragTextureCoord = textureCoord;
    fragToCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPos.xyz;
}