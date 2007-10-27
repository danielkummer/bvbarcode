function [res_img] = imtograyscale(img)
% convert image to grayscaleimage

[x,y,N] = size(img);
if N>1
    res_img = (img(:,:,1) + img(:,:,2) + img(:,:,3));
    grau_max = max(max(max(res_img)));
    res_img = res_img / grau_max;
else
    res_img = img;
end