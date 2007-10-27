function [res_img] = imtobinary(tmp, th)
% convert image to binary image
% maybe optimize it by using a histogramm to calculate a variable threshold

tmp     = double(tmp);
img_max = max(max(max(tmp)));
tmp     = tmp / img_max;
[w,h, c]= size(tmp);
res_img = ones(w,h);

for i=1:w-1
    for j=1:h-1
        if(tmp(i,j) < th)
            res_img(i,j) = 0;
        end
    end
end