function [res_img] = imclean(img)
% filtering

% test: sharpening
laplacianfilter = [0,-1,0; -1, 1+4, -1; 0,-1,0];
res_img = imfilter(img, laplacianfilter, 'replicate');