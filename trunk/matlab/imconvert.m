function [res_img] = imconvert(tmp, th)
%sp�ter vielleicht automatischer threshhold mit historamm

%res_img = tmp;
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
