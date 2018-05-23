
BEGIN;
CREATE TABLE fields (
  fields_id INTEGER PRIMARY KEY,
  fields_name TEXT,
  product_type_id INTEGER
); 

INSERT INTO fields (fields_id, fields_name, product_type_id) VALUES
(1, 'name', 1),
(2, 'price', 1),
(3, 'supplier', 1),
(4, 'description', 1),
(5, 'image_location', 1),
(6, 'author', 2),
(7, 'language', 2);

CREATE TABLE merchant (
  merchant_id INTEGER PRIMARY KEY,
  merchant_name TEXT,
  merchant_motto TEXT,
  merchant_motto_font_color TEXT
); 

INSERT INTO merchant (merchant_id, merchant_name, merchant_motto, merchant_motto_font_color) VALUES
(1, 'Usbong Specialty Bookstore', 'Uplifting Human Lives', '#6f5630'),
(2, 'RetroCC', 'Keep Reading.<br>Keep Collecting.', '#FFFFFF'),
(3, 'Adarna House, Inc', 'Una sa Filipino', '#4f4c41'),
(4, 'Usbong Mart', 'Uplifting Human Lives', '#6f5630'),
(5, 'Marikina Orthopedic Specialty Clinic', 'Marikina Orthopedic Specialty Clinic', '#4f4c41'),
(6, 'Pinoydrones', 'We don''t just sell drones, We sell an experience.', '#FFFFFF');


CREATE TABLE product (
  product_id INTEGER PRIMARY KEY,
  merchant_id INTEGER,
  product_type_id INTEGER,
  name TEXT,
  price REAL,
  previous_price REAL,
  language TEXT,
  author TEXT,
  supplier TEXT,
  description TEXT,
  image_location TEXT,
  format TEXT,
  quantity_in_stock INTEGER,
  translator TEXT,
  product_overview TEXT,
  pages INTEGER,
  product_view_num INTEGER,
  quantity_sold INTEGER,
  external_url TEXT,
  show INTEGER,
  publisher TEXT,
  released_date TEXT,
  is_essential_reading INTEGER,
  expiration TEXT
); 

INSERT INTO product (product_id, merchant_id, product_type_id, name, price, previous_price, language, author, supplier, description, image_location, format, quantity_in_stock, translator, product_overview, pages, product_view_num, quantity_sold, external_url, show, publisher, released_date, is_essential_reading, expiration) VALUES
(487, 5, 13, 'Back Support (Small)', 1700.00, NULL, NULL, NULL, NULL, 'New', NULL, NULL, 1, NULL, 'Back Support (Small)<br><br><strong>Also available:</strong><br><a href=''''https://store.usbong.ph/w/Back-Support-Medium-/488''''>Back Support (Medium)</a><br><a href=''''https://store.usbong.ph/w/Back-Support-Large-/489''''>Back Support (Large)</a><br><a href=''''https://store.usbong.ph/w/Back-Support-Xtra-Large-/490''''>Back Support (Xtra-Large)</a><br>', NULL, 21, 0, NULL, 1, '', '', 0, NULL),
(488, 5, 13, 'Back Support (Medium)', 1700.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, 'Back Support (Medium)<br><br><strong>Also available:</strong><br><a href=''''https://store.usbong.ph/w/Back-Support-Small-/487''''>Back Support (Small)</a><br><a href=''''https://store.usbong.ph/w/Back-Support-Large-/489''''>Back Support (Large)</a><br><a href=''''https://store.usbong.ph/w/Back-Support-Xtra-Large-/490''''>Back Support (Xtra-Large)</a><br>', NULL, 15, 0, NULL, 1, '', '', 0, NULL),
(489, 5, 13, 'Back Support (Large)', 1700.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, 'Back Support (Large)<br><br><strong>Also available:</strong><br><a href=''''https://store.usbong.ph/w/Back-Support-Small-/487''''>Back Support (Small)</a><br><a href=''''https://store.usbong.ph/w/Back-Support-Medium-/488''''>Back Support (Medium)</a><br><a href=''''https://store.usbong.ph/w/Back-Support-Xtra-Large-/490''''>Back Support (Xtra-Large)</a><br>', NULL, 11, 0, NULL, 1, '', '', 0, NULL),
(490, 5, 13, 'Back Support (Xtra-Large)', 1700.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, 'Back Support (Xtra-Large)<br><br><strong>Also available:</strong><br><a href=''''https://store.usbong.ph/w/Back-Support-Small-/487''''>Back Support (Small)</a><br><a href=''''https://store.usbong.ph/w/Back-Support-Medium-/488''''>Back Support (Medium)</a><br><a href=''''https://store.usbong.ph/w/Back-Support-Large-/489''''>Back Support (Large)</a><br>', NULL, 11, 0, NULL, 1, '', '', 0, NULL),
(491, 5, 14, 'Lagaflex (carisoprodol/paracetamol 300/250)', 23.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 1, NULL, 'ANALGESIC/MUSCLE RELAXANT', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2020-09-00T00:00:00+08:00'),
(492, 5, 14, 'Paradrinforte (Para + orphanadrinecitrate)', 19.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 1, NULL, 'ANALGESIC/MUSCLE RELAXANT', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-10-00T00:00:00+08:00'),
(493, 5, 14, 'Proparforte 650/35 (Para + orphanadrinecitrate)', 19.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 800, NULL, 'ANTI-ALLERGIES', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-10-00T00:00:00+08:00'),
(494, 5, 14, 'Loraox (Loratadine 10m)', 7.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 144, NULL, 'ANTI-ALLERGIES', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-05-00T00:00:00+08:00'),
(495, 5, 14, 'Gabaron 300mg (Gabapentin)', 25.00, NULL, 'English', NULL, NULL, '', NULL, NULL, 600, NULL, 'ANTI-CONVULSANT', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-11-00T00:00:00+08:00'),
(496, 5, 14, 'Gabix 100mg (Gabapentin)', 23.90, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 76, NULL, 'ANTI-CONVULSANT', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2020-07-00T00:00:00+08:00'),
(497, 5, 14, 'Gabix 300mg (Gabapentin)', 30.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 57, NULL, 'ANTI-CONVULSANT', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-11-00T00:00:00+08:00'),
(498, 5, 14, 'Adiac 500mg (Metformin 500mg)', 1.70, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 168, NULL, 'ANTI-DIABETIC', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-06-00T00:00:00+08:00'),
(499, 5, 14, 'Zebet 80 (Gliclazide 80mg)', 5.80, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 164, NULL, 'ANTI-DIABETIC', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-04-00T00:00:00+08:00'),
(500, 5, 14, 'Allopurinol 100mg', 1.60, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 560, NULL, 'ANTI-GOUT', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-07-00T00:00:00+08:00'),
(501, 5, 14, 'Colchicine 500mg', 3.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 700, NULL, 'ANTI-GOUT', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-10-00T00:00:00+08:00'),
(502, 5, 14, 'Rhea (colchicine) 500mg', 5.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 100, NULL, 'ANTI-GOUT', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2020-03-00T00:00:00+08:00'),
(503, 5, 14, 'Lodipex 5mg (Amlodipine)', 2.75, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 238, NULL, 'ANTI-HYPERTENSIVE', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-03-00T00:00:00+08:00'),
(504, 5, 14, 'Diadipine 10mg (Amlodipine)', 3.25, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 130, NULL, 'ANTI-HYPERTENSIVE', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-06-00T00:00:00+08:00');

INSERT INTO product (product_id, merchant_id, product_type_id, name, price, previous_price, language, author, supplier, description, image_location, format, quantity_in_stock, translator, product_overview, pages, product_view_num, quantity_sold, external_url, show, publisher, released_date, is_essential_reading, expiration) VALUES
(505, 5, 14, 'Zenobloc 50mg (Atenolol)', 4.50, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 113, NULL, 'ANTI-HYPERTENSIVE', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2018-11-00T00:00:00+08:00'),
(506, 5, 14, 'Zenobloc 100mg (Atenolol)', 6.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 190, NULL, 'ANTI-HYPERTENSIVE', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2018-06-00T00:00:00+08:00'),
(507, 5, 14, 'Amgel 50mg (Losartan)', 11.20, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 132, NULL, 'ANTI-HYPERTENSIVE', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-04-00T00:00:00+08:00'),
(508, 5, 14, 'Losac 100mg (Losartan)', 19.25, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 93, NULL, 'ANTI-HYPERTENSIVE', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-08-00T00:00:00+08:00'),
(509, 5, 14, 'Prolol 50mg (Metoprolol)', 2.60, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 269, NULL, 'ANTI-HYPERTENSIVE', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2018-06-00T00:00:00+08:00'),
(510, 5, 14, 'Prolol 100mg (Metoprolol)', 4.20, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 200, NULL, 'ANTI-HYPERTENSIVE', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-01-00T00:00:00+08:00'),
(511, 5, 14, 'Arcoxia 120mg (Etoricoxib)', 80.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 80, NULL, 'ANTI-INFLAMMATORY/ANALGESIC', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2020-01-00T00:00:00+08:00'),
(512, 5, 14, 'Xibra 60mg (Etoricoxib)', 35.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 57, NULL, 'ANTI-INFLAMMATORY/ANALGESIC', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2020-06-00T00:00:00+08:00'),
(513, 5, 14, 'Dologesic 325/37.5 (Para + Tramadol HCI)', 1.50, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 330, NULL, 'ANTI-INFLAMMATORY/ANALGESIC', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-01-00T00:00:00+08:00'),
(514, 5, 14, 'Cetradol 325/37.5 (Para + Tramadol HCI)', 10.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 600, NULL, 'ANTI-INFLAMMATORY/ANALGESIC', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-05-00T00:00:00+08:00'),
(515, 5, 14, 'Trap 325/37.5 (Para + Tramadol HCI)', 16.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 83, NULL, 'ANTI-INFLAMMATORY/ANALGESIC', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2018-09-00T00:00:00+08:00'),
(516, 5, 14, 'Tramadol 50mg', 5.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 316, NULL, 'ANTI-INFLAMMATORY/ANALGESIC', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2020-09-00T00:00:00+08:00'),
(517, 5, 14, 'Clanza 100mg (Aceclofenac)', 15.20, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 709, NULL, 'ANTI-INFLAMMATORY/ANALGESIC', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2020-11-00T00:00:00+08:00'),
(518, 5, 14, 'Diclotol 100mg (Aceclofenac)', 11.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 48, NULL, 'ANTI-INFLAMMATORY/ANALGESIC', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-12-00T00:00:00+08:00'),
(519, 5, 14, 'Dolowin SR 200mg (Aceclofenac)', 20.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 407, NULL, 'ANTI-INFLAMMATORY/ANALGESIC', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-04-00T00:00:00+08:00'),
(520, 5, 14, 'Dolowin Plus 100mg (Aceclofenac + Para)', 16.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 781, NULL, 'ANTI-INFLAMMATORY/ANALGESIC', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2020-04-00T00:00:00+08:00'),
(521, 5, 14, 'Ibuprofen 200mg', 1.20, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 180, NULL, 'ANTI-INFLAMMATORY/ANALGESIC', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2020-03-00T00:00:00+08:00'),
(522, 5, 14, 'Ibuprofen 400mg', 1.60, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 200, NULL, 'ANTI-INFLAMMATORY/ANALGESIC', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-02-00T00:00:00+08:00'),
(523, 5, 14, 'Ketesse 25mg (Dexcetoprofen)', 28.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 160, NULL, 'ANTI-INFLAMMATORY/ANALGESIC', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-06-00T00:00:00+08:00'),
(524, 5, 14, 'Mefenamic 250mg', 1.50, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 137, NULL, 'ANTI-INFLAMMATORY/ANALGESIC', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2020-08-00T00:00:00+08:00'),
(525, 5, 14, 'Mefenamic 500mg', 2.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 437, NULL, 'ANTI-INFLAMMATORY/ANALGESIC', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2020-09-00T00:00:00+08:00'),
(526, 5, 14, 'Meloxican 15mg', 12.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 1040, NULL, 'ANTI-INFLAMMATORY/ANALGESIC', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-11-00T00:00:00+08:00'),
(527, 5, 14, 'Naproxen 500mg', 7.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 150, NULL, 'ANTI-INFLAMMATORY/ANALGESIC', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2020-01-00T00:00:00+08:00'),
(528, 5, 14, 'Subsyde CR 100mg (Diclofenac Na)', 10.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 2939, NULL, 'ANTI-INFLAMMATORY/ANALGESIC', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-01-00T00:00:00+08:00'),
(529, 5, 14, 'Zornica - 4 (Lornoxican)', 16.50, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 155, NULL, 'ANTI-INFLAMMATORY/ANALGESIC', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2018-12-00T00:00:00+08:00'),
(530, 5, 14, 'Amoxicillin 500mg', 4.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 166, NULL, 'ANTI-INFECTIVES/BACTERIA', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2020-10-00T00:00:00+08:00'),
(531, 5, 14, 'Cefalaxin 500mg', 7.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 181, NULL, 'ANTI-INFECTIVES/BACTERIA', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2020-08-00T00:00:00+08:00'),
(532, 5, 14, 'Cefixin 200mg', 25.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 100, NULL, 'ANTI-INFECTIVES/BACTERIA', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2020-03-00T00:00:00+08:00'),
(533, 5, 14, 'Cefuroxine 500mg', 46.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 48, NULL, 'ANTI-INFECTIVES/BACTERIA', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-04-00T00:00:00+08:00'),
(534, 5, 14, 'Ciprofloxacin 500mg', 7.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 150, NULL, 'ANTI-INFECTIVES/BACTERIA', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2020-03-00T00:00:00+08:00'),
(535, 5, 14, 'Clindamycin 300mg', 8.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 155, NULL, 'ANTI-INFECTIVES/BACTERIA', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-12-00T00:00:00+08:00'),
(536, 5, 14, 'Cloxacillin 500mg', 5.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 137, NULL, 'ANTI-INFECTIVES/BACTERIA', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2018-07-00T00:00:00+08:00'),
(537, 5, 14, 'Co Amoxiclav 625mg', 25.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 164, NULL, 'ANTI-INFECTIVES/BACTERIA', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-10-00T00:00:00+08:00'),
(538, 5, 14, 'Doxycycline Hyclate 100mg', 2.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 23, NULL, 'ANTI-INFECTIVES/BACTERIA', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2018-07-00T00:00:00+08:00'),
(539, 5, 14, 'Ofloxacin 200mg', 10.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 120, NULL, 'ANTI-INFECTIVES/BACTERIA', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-10-00T00:00:00+08:00'),
(540, 5, 14, 'Ofloxacin 400mg', 13.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 73, NULL, 'ANTI-INFECTIVES/BACTERIA', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-01-00T00:00:00+08:00'),
(541, 5, 14, 'Aldren 70mg (Alendronete Sodium)', 145.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 92, NULL, 'ANTI-OSTEOPOROSIS', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-04-00T00:00:00+08:00'),
(542, 5, 14, 'Alendra 70mg (Alendronete Sodium)', 280.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 78, NULL, 'ANTI-OSTEOPOROSIS', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-05-00T00:00:00+08:00'),
(543, 5, 14, 'Reventa 70mg (Alendronete Sodium)', 158.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 44, NULL, 'ANTI-OSTEOPOROSIS', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-04-00T00:00:00+08:00'),
(544, 5, 14, 'Prednisone 5mg', 1.30, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 400, NULL, 'CORTICOSTEROID', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2018-09-00T00:00:00+08:00'),
(545, 5, 14, 'Omeprazele 20mg', 7.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 72, NULL, 'HYPERACIDITY (PANGANGASIM NG SIKMURA)', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2020-02-00T00:00:00+08:00'),
(546, 5, 14, 'Omeprazele 40mg', 10.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 119, NULL, 'HYPERACIDITY (PANGANGASIM NG SIKMURA)', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2020-04-00T00:00:00+08:00'),
(547, 5, 14, 'Agmaset 44mg', 25.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 160, NULL, 'SUPPLEMENT FOR JOINT & BONE', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2020-01-00T00:00:00+08:00'),
(548, 5, 14, 'Alanerve', 65.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 165, NULL, 'SUPPLEMENT FOR JOINT & BONE', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2018-06-00T00:00:00+08:00'),
(549, 5, 14, 'Artiflex', 16.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 780, NULL, 'SUPPLEMENT FOR JOINT & BONE', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-05-00T00:00:00+08:00'),
(550, 5, 14, 'Calcium Plus 800/6 (KIRKLAND)', 6.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 1, NULL, 'SUPPLEMENT FOR JOINT & BONE', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2020-08-00T00:00:00+08:00'),
(551, 5, 14, 'CALCIUMADE', 9.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 2800, NULL, 'SUPPLEMENT FOR JOINT & BONE', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-08-00T00:00:00+08:00'),
(552, 5, 14, 'Glucosamine Sulfate 500mg (Exact)', 12.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 1, NULL, 'SUPPLEMENT FOR JOINT & BONE', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-12-00T00:00:00+08:00'),
(553, 5, 14, 'Glucosamine Sulfate 750mg (KIRKLAND)', 10.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 1, NULL, 'SUPPLEMENT FOR JOINT & BONE', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2021-03-00T00:00:00+08:00'),
(554, 5, 14, 'Piascledine', 38.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 945, NULL, 'SUPPLEMENT FOR JOINT & BONE', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2020-01-00T00:00:00+08:00'),
(555, 5, 14, 'Viartril-S 500mg', 20.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 2467, NULL, 'SUPPLEMENT FOR JOINT & BONE', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2018-12-00T00:00:00+08:00'),
(556, 5, 14, 'Viartril-S Sachet', 60.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 242, NULL, 'SUPPLEMENT FOR JOINT & BONE', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-11-00T00:00:00+08:00'),
(557, 5, 14, 'Sovit-Cee 500mg (Sodium Ascorbate)', 5.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 226, NULL, 'VITAMINS', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-01-00T00:00:00+08:00'),
(558, 5, 14, 'Fastum Gel 30g', 430.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 1, NULL, 'ANTI-INFLAMMATORY GEL/CREAM', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2022-05-00T00:00:00+08:00'),
(559, 5, 14, 'Dicloran Gel 20g (Diclofenac)', 250.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 15, NULL, 'ANTI-INFLAMMATORY GEL/CREAM', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-11-00T00:00:00+08:00'),
(560, 5, 14, 'Reparil-N Gel 40g', 280.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 44, NULL, 'ANTI-INFLAMMATORY GEL/CREAM', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2022-03-00T00:00:00+08:00'),
(561, 5, 14, 'Vigel Cream 15g', 150.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 240, NULL, 'ANTI-INFLAMMATORY GEL/CREAM', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2020-07-00T00:00:00+08:00'),
(562, 5, 14, 'Dehydrosol Powd.', 15.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 25, NULL, 'ANTI-INFLAMMATORY GEL/CREAM', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2021-04-00T00:00:00+08:00'),
(563, 5, 14, 'Synvisc 3 inj.', 25000.00, NULL, 'English', NULL, NULL, NULL, NULL, NULL, 9, NULL, 'ANTI-INFLAMMATORY GEL/CREAM', NULL, 0, 0, NULL, 1, NULL, NULL, 0, '2019-11-00T00:00:00+08:00');




CREATE TABLE product_type (
  product_type_id INTEGER PRIMARY KEY,
  product_type_name TEXT
); 

INSERT INTO product_type (product_type_id, product_type_name) VALUES
(1, 'All'),
(2, 'Books'),
(3, 'Beverages'),
(4, 'Books/Beverages'),
(5, 'Promos'),
(6, 'Comics'),
(7, 'Manga'),
(8, 'Toys & Collectibles'),
(9, 'Textbooks'),
(10, 'Children''s'),
(11, 'Food'),
(12, 'Miscellaneous'),
(13, 'NON-MED'),
(14, 'MED');

CREATE TABLE cart (
  cart_id INTEGER PRIMARY KEY,
  product_id INTEGER,
  quantity INTEGER,
  price REAL,
  purchased_datetime_stamp TEXT
); 

COMMIT;