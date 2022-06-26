 (Created in July 2021)
 
 Program for rendering geometrical objects using "ray marching" method. Extended in 4D euclidean space.
 Inspired by Onigiri: https://www.youtube.com/watch?v=OnLeOmlLpHc&t=542s&ab_channel=Onigiri
 
 Main logic of this program is described in fragment shader using glsl: app/src/main/assets/shaders/fragment.txt
 
 
 In order to achieve this result I managed to derive 4D case of Rodrigue's Rotation formula ('cause I failed to google it):

- <img src="https://latex.codecogs.com/svg.image?M(a,b,\varphi&space;)&space;=&space;I&plus;sin(\varphi)*K&plus;(1.0-cos(\varphi&space;))*K^2"/>,
Where:
- <img src="https://latex.codecogs.com/svg.image?K&space;=&space;\begin{bmatrix}0&space;&a.w*b.z-a.z*b.w&space;&&space;a.y*b.w-a.w*b.y&space;&&space;a.z*b.y-a.y*b.z\\a.z*b.w-a.w*b.z&space;&&space;0&space;&&space;a.w*b.x-a.x*b.w&space;&&space;a.x*b.z-a.z*b.x\\a.w*b.y-a.y*b.w&space;&&space;a.x*b.w-a.w*b.x&space;&&space;0&space;&&space;a.y*b.x-a.x*b.y\\ya.y*b.z-a.z*b.y&space;&&space;a.z*b.x-a.x*b.z&space;&&space;a.x*b.y-a.y*b.x&space;&&space;0\\\end{bmatrix};"/>

a, b are orthogonal vectors that describes static 2D plane of 4D rotation (analog of axis of 3D rotation);
angle is an angle of rotation in radians;
M is 4D rotation matrix.

 Results:
 
 
 1) Different parallel 3D sections of 4D Hypercube:
 ![Alt Text](https://github.com/mTerentev/AndroidProjects/blob/main/Render4D/Screen%20records/Screen_Recording_20210711-160919_Shader-Editor-_online-video-cutter.com_.gif)
 ![Alt Text](https://github.com/mTerentev/AndroidProjects/blob/main/Render4D/Screen%20records/Screen_Recording_20210713-171953_Shader-Editor-_online-video-cutter.com_.gif)
 
 
 2) Parallel 3D sections of Hyperball:
 ![Alt Text](https://github.com/mTerentev/AndroidProjects/blob/main/Render4D/Screen%20records/Screen_Recording_20210711-161223_Shader-Editor-_online-video-cutter.com_.gif)
 
 
 3) A section of "Hypercylinder" (4D object constructed by sweeping a 3D ball along w-axis). Rotating around x-w plane:
 ![Alt Text](https://github.com/mTerentev/AndroidProjects/blob/main/Render4D/Screen%20records/Screen_Recording_20210714-141949_Shader-Editor-_online-video-cutter.com_.gif)
