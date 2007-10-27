function [res_img] = imclean(img)
%filterung (median / minmax)

%TEST: sharpening
laplacianfilter = [0,-1,0; -1, 1+4, -1; 0,-1,0];
res_img = imfilter(img, laplacianfilter, 'replicate');