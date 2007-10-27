%------- testimages --------------------------------------------------
%img = imread('testimages/0011_rot_klein.bmp');
%img = imread('testimages/1925_senkrecht.bmp');
%img = imread('testimages/0011_rot3.bmp');
%img = imread('testimages/0011_gross.bmp');
%img = imread('testimages/0011_rot_schlecht2.bmp');
%img = imread('testimages/0011_rot_schlecht3.bmp');
%img = imread('testimages/0011_rot_schlecht.bmp');
%img = imread('testimages/0011_rot2.bmp');
%img = imread('testimages/int25-1.bmp');
%img = imread('testimages/0011_senkrecht_aufkopf.bmp');
%img = imread('testimages/0011_rot90.bmp');
%img = imread('testimages/0011_rot270.bmp');

img = imread('testimages/1925_horizontal.bmp');

figure(1)
subplot(221)
imshow(img, [])
title('Originalbild')

% test: blurring image -> is compensated by imclean()
% gaussian filter with sigma=1
gaussianfilter = fspecial('gaussian', [7,7], 1);
img = imfilter(img, gaussianfilter, 'replicate');

% preliminary double conversion
img = double(img);
[w,h,N] = size(img);

%------- 1. to grayscale --------------------------------------------------
img = imtograyscale(img);

%------- 2. denoise -------------------------------------------------------
img = imclean(img);

subplot(222)
imshow(img, [])
title('Grayscale Bild, gefiltert')

%------- 3. convert to binary ---------------------------------------------
img = imtobinary(img, 0.5);

subplot(223)
imshow(img, [])
title('Binärbild')

%------- 4. scan for barcode using the bresenham line algorithm------------
lines = [int16(w/4), 1, int16(w/4), h ;
         int16(w/2), 1, int16(w/2), h ;
         int16(3*w/4), 1, int16(3*w/4), h;
         1, int16(h/4), w, int16(h/4);
         1, int16(h/2), w, int16(h/2);
         1, int16(3*h/4), w, int16(3*h/4);
         1, 1, w, h;
         int16(w/2), 1, w, int16(h/2);
         1, int16(h/2), int16(w/2), h;
         int16(w/2), h, w, int16(h/2);
         1, int16(h/2), int16(w/2), 1;
         1, h, w, 1;
         ];

line_nr = 0;
result = [-1];

while  length(result)==1  && line_nr < 12
    line_nr = line_nr + 1;
%------- 5. count pixel on scanline ---------------------------------------    
    [bc_vector, img] = pixelcount(img, lines(line_nr,:) );
%------- 6. convert to code -----------------------------------------------
    code_vec = get_code(bc_vector, 1.6)
%------- 7. decode code to numbers ----------------------------------------
    result = decode(code_vec);
end

subplot(224)
imshow(img);
title('Resultat mit Scanlinien')
result

