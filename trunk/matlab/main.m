%img = imread('0011_rot_klein.bmp');
%img = imread('1925_senkrecht.bmp');
%img = imread('0011_rot3.bmp');
%img = imread('0011_gross.bmp');
img = imread('0011_rot_schlecht2.bmp');
%img = imread('0011_rot_schlecht3.bmp');
%img = imread('0011_rot_schlecht.bmp');
%img = imread('0011_rot2.bmp');
%img = imread('int25-1.bmp');
%img = imread('0011_senkrecht_aufkopf.bmp');
%img = imread('0011_rot90.bmp');
%img = imread('0011_rot270.bmp');

figure(1)
subplot(221)
imshow(img, [])
title('Originalbild')


%TEST: verschmieren -> funktioniert bei den meisten nicht
%gaussfilter mit sigma=1
gaussianfilter = fspecial('gaussian', [7,7], 1);
img = imfilter(img, gaussianfilter, 'replicate');
%img = imfilter(img, gaussianfilter, 'replicate');

%double konvertierung
img = double(img);
%------- 1. to grayscale --------------------------------------------------
img = imtograyscale(img);

%------- 2. denoise -------------------------------------------------------
img = imclean(img);

subplot(222)
imshow(img, [])
title('Grayscale Bild, gefiltert')

%------- 3. convert to binary ---------------------------------------------
img = imconvert(img, 0.5);

subplot(223)
imshow(img, [])
title('Binärbild')

%bereich suchen

[w,h,N] = size(img);
%accumulator = [];


% %Barcode suchen
% startpos = [0,0];
% endpos   = [0,0];
% 
% x = 128;
% %for x=1:w
%     for y=1:h
%         if(img(x,y) == 0)
%             startpos = [x,y]; %Wegen Randpixelproblemen irgendwo in die Mitte des Barcodes springen
%             break %schwarzer Pixel gefunden
%         end
%     end
%     
%     for y=h:-1:startpos(2)
%         if(img(x,y) == 0)
%             endpos = [x,y];
%             break
%         end
%     end
%%end

%endpos = [x, 1];
%startpos = [x+50, h-1];

%------- 4. scan for barcode ----------------------------------------------

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
%[bc_vector, img] = pixelcount(img, lines(1, :) );
%[nx, ny] = size(bc_vector);
%line_nr = 1;
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

% 
% for y = startpos(2):endpos(2)
%     img(x-1, y) = 128;
% end
%bc_vector
%size(bc_vector)
subplot(224)
imshow(img);
title('Resultat mit Scanlinien')
result

