function [res_img] = imconvert(tmp, th)
%später vielleicht automatischer threshhold mit historamm

%TEST: sharpening
%laplacianfilter = [0,-1,0; -1, 1+4, -1; 0,-1,0];
%tmp = imfilter(tmp, laplacianfilter, 'replicate');

res_img = tmp;

tmp = double(tmp);
img_max = max(max(max(tmp)));
tmp = tmp / img_max;
[w,h, c] = size(tmp);
res_img = ones(w,h);

for i=1:w-1
    for j=1:h-1
        if(tmp(i,j) < th)
            res_img(i,j) = 0;
        end
    end
end
figure(333)
imshow(tmp, [])