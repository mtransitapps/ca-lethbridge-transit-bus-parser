package org.mtransit.parser.ca_lethbridge_transit_bus;

import java.util.HashMap;

public class Stops {
	public static HashMap<String, String> ALL_STOPS;
	static {
		HashMap<String, String> allStops = new HashMap<String, String>();
		// rm -r input/gtfs
		// unzip input/gtfs.zip -d input/gtfs
		// awk -F "\"*,\"*" '{print "allStops.put(\"" $2 "\", \"" $1"\"); // " $3}' input/gtfs/stops.txt > output/output.java
		allStops.put("11052", "a9d08d50-5914-4bb3-96a6-6e6347d04969"); // North Terminal
		allStops.put("11059", "42ce5df0-e942-4de6-8e19-bcdd964042d4"); // 23 AVE N & 13 ST N
		allStops.put("11060", "5c766c82-6d60-4285-98f3-85e12d01cb41"); // 1500 23 AVE N
		allStops.put("11061", "003c2cdb-3d7f-40f9-80e3-404605174018"); // 18 ST N & 23 AVE N
		allStops.put("11064", "2d90bc7b-21e6-4ed8-9ac4-33a5b305ba9f"); // 18 AVE N & 18 ST N
		allStops.put("11066", "900911f1-a34a-45e8-9619-aec8f37d5fba"); // 18 AVE N & 20 ST N
		allStops.put("11067", "54b01e21-b82e-4736-93e3-d2476f08ffe4"); // 23 ST N & 28 AVE N
		allStops.put("11069", "6c1f3163-7530-4925-bc0f-d69eb4c89dd2"); // 15 Ave & 22 St N
		allStops.put("11071", "6749e2af-ffc6-4284-94f6-ba3d525156c0"); // 13 AVE N & 20 ST N
		allStops.put("11223", "c3bd738f-ad60-4afa-ac7d-31364a5a370a"); // 13 AVE N & 18 ST N
		allStops.put("11074", "4511c337-496f-46b2-a3de-4ac0ea72ea42"); // 15 AVE N & 18 ST N
		allStops.put("11075", "3e235fb5-0bc5-47bc-bd17-e70fed01513e"); // 16 ST N & 15 AVE N
		allStops.put("11077", "45617d08-0899-4f34-b408-da29ffcb35b2"); // 18 AVE N & 16 ST N
		allStops.put("11079", "7e3f71c4-458a-4958-9669-88c84812f060"); // 18 AVE N & 13 ST N
		allStops.put("11082", "7ec57d15-324c-40d2-ab95-d0b8e10a621b"); // 18 AVE N & ST. CHRISTOPHER PL
		allStops.put("11084", "e4d4fd4a-4544-4d93-80d5-e3fb7145da45"); // ST. EDWARD BLVD N & STAFFORD DR N
		allStops.put("11085", "d08bdbf2-9fee-4af4-87da-ef2888ba03fa"); // STAFFORD AVE N & STAFFORD DR N
		allStops.put("11088", "fd775dea-cff2-4287-8011-c99c2141079c"); // STAFFORD AVE N & 7 ST N
		allStops.put("11090", "3614351b-4ce4-46bd-b139-9e53f97db6f3"); // 6 ST N & 12 AVE N
		allStops.put("11092", "59cd8ad5-1401-4bc1-a275-409d0e0505e8"); // 6 ST N & 9 AVE N
		allStops.put("11253", "0e9fd2a8-519d-49fd-b0f9-4efc03fd7767"); // 9 AVE N & 7 ST N
		allStops.put("11096", "c4aa0118-3b99-47f5-af76-a53b6468f201"); // 9 AVE N & 9 ST N
		allStops.put("11099", "865eb9e3-148b-4e45-9238-72e1d1846a34"); // 9 AVE N & 12 ST N
		allStops.put("11102", "bf73bc80-e0ea-4fe2-9265-5e83f940c0a5"); // 12 ST N & 8 AVE N
		allStops.put("11104", "2ea95c0f-ca3a-4188-a691-b022a80bea56"); // 12 ST N & 6 AVE N
		allStops.put("11106", "d2e5a5b7-25a6-4ed2-870b-4b7a34659e4b"); // 5 AVE N & 12A ST S
		allStops.put("11107", "ba007b44-e5c5-491e-a48b-a3a7dca1e02b"); // 5 AVE N & 10 ST N
		allStops.put("11109", "52ab5088-886a-4f7c-bdcf-e72ae44c7d52"); // 9 ST S & 4 AVE S
		allStops.put("14003", "170e8233-d370-44a8-b6ca-9465915c08ac"); // 1 AVE S & 8 ST S
		allStops.put("14005", "b6f81954-4ebd-4200-a3b3-e346e263ea15"); // 1 AVE S & 5 ST S
		allStops.put("14007", "582a9687-caa1-4391-af21-463e7fe2139d"); // 3 ST S & 1 AVE S
		allStops.put("14013", "ad4c39ab-068a-4c73-8ab3-72444610c067"); // 4 AVE S & 3 ST S
		allStops.put("14015", "0935ac20-c3b9-42e8-9804-6188e69c384b"); // City Centre Terminal
		allStops.put("13002", "92bbdbe6-44db-4841-9517-2bcecc71ec60"); // University Terminal
		allStops.put("13006", "7a1d6a2e-bed6-4192-a076-67abfc2efbd1"); // Aperture Drive W
		allStops.put("13007", "a72445e8-7815-46b5-b99a-21319ed29a0f"); // UNIVERSITY DR W & VALLEY RD W
		allStops.put("13008", "aadf5171-42eb-4b52-9b90-78f7cd6c76c3"); // UNIVERSITY DR W & RIDGEWOOD BLVD W
		allStops.put("13009", "30a1a850-7f51-4586-90ab-56be0d08989a"); // University Dr W & Edgewood Blvd W
		allStops.put("13011", "1ff63ea0-f0c5-43e6-9597-1320be6db25d"); // HERITAGE BLVD W & UNIVERSITY DR W
		allStops.put("13012", "e3c8287c-cefc-4e56-b26b-a1a5871ef187"); // HERITAGE BLVD W & HERITAGE CL W
		allStops.put("13013", "609cedc7-54cd-4c2f-ae1b-195d1f9baa1b"); // HERITAGE BLVD W & HERITAGE CL W
		allStops.put("13014", "caa03f0a-c4c9-4c03-a0bf-d3139e8b6645"); // HERITAGE BLVD W & HERITAGE PL W
		allStops.put("13015", "a4075e75-1306-4267-b310-a08fc37a3345"); // HERITAGE BLVD W & HERITAGE PT W
		allStops.put("13016", "745debc7-2af1-430b-8806-d0de254bb027"); // HERITAGE BLVD W & HERITAGE PT W
		allStops.put("13017", "9a353d36-7148-4d08-a3d6-f9c5f5c86351"); // HERITAGE BLVD W & HERITAGE LANE W
		allStops.put("13018", "4957d567-5ce2-430e-ad93-4f9912065d48"); // HERITAGE BLVD W & HERTITAGE LANE W
		allStops.put("13019", "e26c7cc9-0ac9-4338-9860-9bbd61561519"); // HERITAGE BLVD W & HERITAGE CRT W
		allStops.put("13020", "042d5313-d6b8-4890-ac28-17316bad5d5a"); // HERITAGE BLVD & HERITAGE HEIGHTS PLAZA W
		allStops.put("13021", "0008a542-c9ee-4fbd-b5d7-05b4e3c23bf5"); // WALSH DR W & WESTSIDE DR W
		allStops.put("13400", "2a669238-5956-4edd-bdaf-266ad84d89b2"); // ABERDEEN GATE W & WALSH DR W
		allStops.put("13401", "a5aeac4b-108e-41b7-a98a-9050632d8c89"); // ABERDEEN RD W & ABERDEEN CRES W
		allStops.put("13402", "eb7b73df-68f2-4b71-9806-4708930b1ca0"); // ABERDEEN RD W
		allStops.put("13403", "8ba9846f-071c-4967-a090-5f13697f42b0"); // ABERDEEN GATE W & TARTAN BLVD W
		allStops.put("13404", "b430e262-918c-4225-8748-c58117d1abc5"); // ABERDEEN RD W & EDINBURGH RD W
		allStops.put("13022", "8148d496-55a5-43ed-99a1-bf6cb508fb66"); // HIGHLANDS BLVD W & TARTAN BLVD W
		allStops.put("13023", "0a385aa7-80d5-494b-b791-31b547756c7b"); // HIGHLANDS BLVD W & ANGUS RD W
		allStops.put("13024", "57f25197-d502-4f74-b1a6-324cab9268a3"); // Highlands Rd & Highlands Blvd W
		allStops.put("13059", "c7669b56-6982-49a0-814a-19b6906a0ce8"); // Highlands Rd & University Dr W
		allStops.put("13060", "6ce88e94-ef34-47e9-b4cf-64d054bc6a4a"); // University Dr & Garry Dr W
		allStops.put("13010", "f4a9b8f6-b83e-4c13-b13a-b33b24cd6e31"); // SHERWOOD BLVD & EDGEWOOD BLVD W
		allStops.put("13452", "bcbfb82a-0941-44cf-83ad-0ef031071755"); // RIDGEWOOD BLVD & UNIVERSITY DR W
		allStops.put("13530", "d7eaa368-71ee-4359-bf15-424a93b33871"); // University Dr W
		allStops.put("13061", "2e63b041-6a47-479b-a7d4-c075b1458b20"); // UNIVERSITY DR W & COLUMBIA BLVD W
		allStops.put("14011", "d167f139-e0f7-425f-bc65-86de665f6cbf"); // City Centre Terminal
		allStops.put("12176", "47ba5074-4812-4d8e-86bc-3b788c19b275"); // 5 ST S & 7 AVE S
		allStops.put("12174", "630a320f-5ce7-4ad4-9840-ffce97fea9dd"); // 9 AVE S & 8 ST S
		allStops.put("12047", "0715940e-4b08-4984-a626-8c4400e68490"); // 9 AVE S & 11 ST S
		allStops.put("12048", "8d9276b1-096d-4885-99b3-db36f9ad527f"); // 9 AVE S & 13 ST S
		allStops.put("12168", "d13cdb3c-ec2b-4825-8c80-a53730a76fee"); // 10 AVE S & 13 ST S
		allStops.put("12166", "45a6bd3b-c87d-4576-837d-65c38f3fa595"); // 10 AVE S & 13 ST S
		allStops.put("12165", "644fca5d-2f56-4a86-bb00-3eb341a0e452"); // 10 AVE S & 10 st s
		allStops.put("12162", "15be24ae-1e0e-439c-9169-cf3a977e86af"); // 20 ST S & 10 AVE S
		allStops.put("12163", "88c7b09d-6131-4041-940c-e928d64f2240"); // 20 ST S & 11 AVE S
		allStops.put("12144", "c6d67e40-fe10-46c3-ba65-312c813df3f6"); // 12 AVE S & 20 ST S
		allStops.put("12145", "cf1ccab1-57f9-421d-99ac-495086b3da92"); // 12 AVE S & 23 ST S
		allStops.put("12146", "1c258660-95c0-4fa8-80ae-ec1cfaf60991"); // 12 AVE S & MAYOR MAGRATH DR S
		allStops.put("12211", "b165328b-c085-47cd-8cb5-6e52b757df62"); // MAYOR MAGRATH DR S & 14 AVE S
		allStops.put("12212", "1bf629fc-1b61-414b-ace8-ea8d1f5ae933"); // MAYOR MAGRATH DR S & 16 AVE S
		allStops.put("12147", "81694f75-363b-4d07-be11-275fed0a22f1"); // 16 AVE S & 26 ST S
		allStops.put("12148", "ca103d9d-3028-4b68-91ca-baca1140486c"); // 16 AVE S & 24 ST S
		allStops.put("12149", "49910a5b-5f1b-4207-8fae-a1f59e7516c8"); // 16 AVE S & 22 ST S
		allStops.put("12140", "817005b0-a058-4ae8-8a0a-be1db4454102"); // 20 ST S & 19 AVE S
		allStops.put("12139", "64cff25a-14de-48c5-850b-41415a80fb92"); // 20 ST S & 20 AVE S
		allStops.put("12137", "b4f8d0d3-d06b-4923-b71c-7baa0752d7ad"); // 20 ST S & 16 AVE S
		allStops.put("12186", "29d2afe8-56d4-49ec-a99c-dab57c29d531"); // SCENIC DR S & TUDOR CRES S
		allStops.put("12101", "4ba1ca10-f3b1-4e4d-85ac-926256a2914e"); // SCENIC DR S & 28 ST S
		allStops.put("12121", "fa7bedfa-cda1-46bf-b181-171475f9c189"); // FAIRWAY PLAZA RD S & FAIRMONT BLVD S
		allStops.put("12122", "782f791e-0091-467b-bad6-861e477eebf6"); // FAIRMONT BLVD S & FAIRWAY PLAZA RD S
		allStops.put("12113", "dcf1a564-c140-4846-8d37-36e25892e481"); // FAIRMONT RD S & FAIRWAY RD S
		allStops.put("12114", "48089217-465e-4ab0-be2d-25db36493f0e"); // FAIRMONT BLVD S & FAIRMONT LINK S
		allStops.put("12115", "b61b60ac-b6d7-4bb8-aace-be176f4423f5"); // FAIRMONT BLVD S & FAIRMONT CRT S
		allStops.put("12116", "50d0ffd9-d6c0-4710-a886-ae2d478e6880"); // FAIRMONT BLVD S & FAIRMONT COVE S
		allStops.put("12329", "fe3850d9-68c6-4eea-9d22-eb39f5b5a044"); // FAIRMONT BLVD S & FAIRMONT GARDEN RD S
		allStops.put("12123", "7ff41256-2749-48ad-bf35-80af7a5a70b8"); // FAIRMONT BLVD S & FARIMONT PARKLANDS
		allStops.put("12124", "51620a04-ab19-4030-bbc5-4616e210470c"); // FAIRMONT BLVD S & FAIRMONT PARK LANES
		allStops.put("12125", "84e47074-499f-4ab9-b88d-c98abbbd5b4f"); // FAIRMONT BLVD S & FAIRWAY ST S
		allStops.put("12126", "0125f9ea-535b-4191-ac43-2c60608f91fb"); // MAYOR MAGRATH DR S & 32 AVE S
		allStops.put("12127", "b1d8e7c9-eeae-4f1d-b6a1-259ee8d13936"); // MAYOR MAGRATH DR & 34 AVE S - WALMART
		allStops.put("12300", "2a7b0bfb-1e86-4921-b1fd-0110924817e3"); // 40 AVE & MAYOR MAGRATH DR S
		allStops.put("12343", "c81b0a7e-fd48-4b2d-ae41-b8e583b6cc87"); // 40 AVE & 6 MILES RD S
		allStops.put("12326", "f41bcfa5-019c-40d6-85b4-3f8134a8341e"); // COULEESPRINGS RD S
		allStops.put("12344", "ba761e82-be97-4865-afc6-824b1566260f"); // COULEESPRINGS LINK S & COULEESPRINGS WAY S
		allStops.put("12327", "9ac096ca-6ba8-4464-92b3-00e4d74c0be3"); // SOUTHGATE BLVD S
		allStops.put("12328", "f8f670c8-061a-4acd-862f-515beb4dd2c5"); // SOUTHGATE BLVD S
		allStops.put("12119", "93f5de73-8e3c-4fab-b376-9b54d3e99452"); // SOUTHGATE BLVD S & COULEECREEK BLVD S
		allStops.put("12120", "82975b4d-9bb4-4b88-956b-ef667aee503c"); // SOUTHGATE BLVD S & MAYOR MAGRATH DR S
		allStops.put("12129", "75d8b719-43bb-4c9c-9382-3f2fe7a535da"); // 28 AVE S & 32 st s
		allStops.put("12130", "c98cde07-dd84-4dd8-83c9-7994e14bae07"); // 28 AVE S & 28 ST S
		allStops.put("12217", "ef1d1805-c6ab-4eb6-995d-c775b3cd28ce"); // Lethbridge College
		allStops.put("14024", "16371cdf-1f79-4734-814f-ac1eafa80646"); // STAFFORD DR S & 5 AVE S
		allStops.put("12334", "1be59f7f-d97c-4339-8825-c89b3a765d27"); // 6 AVE S & 11 ST S
		allStops.put("12335", "5e6234b3-4275-454b-9b12-105b57891d6c"); // 6 AVE S & 12 ST S
		allStops.put("12336", "99065107-39a7-450a-8654-bcbe60485ffc"); // 6 AVE S & 13 ST S
		allStops.put("12196", "ba3ad5d0-2d67-4ac2-ac47-caf700fcf9e3"); // 13 ST S & 7 AVE S
		allStops.put("12049", "fef53962-f875-4f1a-8bd2-73be989d95e6"); // 9 AVE S & 13 ST S
		allStops.put("12051", "13665863-5800-4c00-b5d0-dca9ab0e67be"); // 9 AVE S & 16 ST S
		allStops.put("12055", "43d081fd-a479-4591-8d23-cee9de6c3ffc"); // 9 AVE S & 18 ST S
		allStops.put("12058", "dc646d3d-50ae-4b84-b485-84ffa24bb552"); // 9 AVE S & 21 ST S
		allStops.put("12060", "ce4c6f4d-8287-4670-a4c4-03bec1d1e5c2"); // 9 AVE S & MAYOR MAGRATH DR S
		allStops.put("12208", "753d3677-9036-41a7-a80e-ba837ced8f0b"); // MAYOR MAGRATH DR S & 9 AVE S
		allStops.put("12209", "296f5d7d-b78f-4a12-8220-8211724e865d"); // MAYOR MAGRATH DR S & 10 AVE S
		allStops.put("12210", "bd8819e3-82bc-4bcc-8314-3674188dfbf8"); // MAYOR MAGRATH DR S & 12 AVE S
		allStops.put("12999", "b1f51db6-87e9-4805-ba1b-2bd4041f266c"); // 12 AVE S & 28 ST S
		allStops.put("12338", "8d08a7d7-fb89-4609-b146-588d0dedf432"); // 12 AVE S & 30 ST S
		allStops.put("12068", "7cc011ff-e0f7-4c0f-ab2e-f3010cba6ac1"); // HENDERSON LAKE BLVD S & 31A ST S
		allStops.put("12070", "7628d8c4-afc1-454a-af1c-d4202f48ef3b"); // HENDERSON LAKE BLVD S & SOUTH PARKSIDE S
		allStops.put("12071", "f77b5b41-f6b8-4fa5-9edf-ade5164d7406"); // HENDERSON LAKE BLVD S & PARKSIDE DR S
		allStops.put("12073", "6ecc6a55-bdba-4253-a723-aab4088a52e1"); // SOUTH PARKSIDE DR S & LAKEWAY BLVD S
		allStops.put("12076", "38829bcf-51b0-4f7f-8acf-3a61f964d5c2"); // GREAT LAKES RD S & GLACIER AVE S
		allStops.put("12077", "15f55f52-de08-48ed-99e3-7c7181c6fc66"); // LAKEMOUNT BLVD S & GREAT LAKES RD S
		allStops.put("12078", "647ac486-c5ff-412b-b95b-2b6f2a6c651c"); // LAKEMOUNT BLVD S & GREAT LAKES RD S
		allStops.put("12079", "4476b36f-14c8-4d56-94d0-54bfa6840e7e"); // LAKEMOUNT BLVD S & SYLVAN RD S
		allStops.put("12081", "4c61d723-8639-43d3-b392-7ffb2d8c7f88"); // FORESTRY AVE S & LAKEMOUNT BLVD S
		allStops.put("12083", "87e883e3-3c5e-420d-af90-5892514dad84"); // FORESTRY AVE S & ASPEN PL S
		allStops.put("12085", "0add83fc-32eb-41d2-b202-e2db77d44d87"); // CEDAR RD S & OAK DR S
		allStops.put("12087", "b482d3ed-2057-450c-8d8c-96113ec52ceb"); // CEDAR RD S & 20 AVE S
		allStops.put("12089", "7dd5cac1-6b8b-413c-8d4f-5f7045818093"); // 20 AVE S & 37 ST S
		allStops.put("12092", "ef880f83-90e5-431a-ae82-22a8ea1e0547"); // 20 AVE S & 35 ST S
		allStops.put("12093", "45965973-abc2-4606-baba-59baaef38cde"); // 20 AVE S & LAKEMOUNT BLVD S
		allStops.put("12099", "1395e793-bc61-43dc-9144-cc4b7ef04056"); // 20 AVE S & MAYOR MAGRATH
		allStops.put("12214", "689fd65e-a640-44c7-8d29-eb6f3d911e8e"); // MAYOR MAGRATH DR S & 22 AVE S
		allStops.put("12100", "68539b58-3e71-4466-8da7-c3ee7171b359"); // SCENIC DR S & 28 ST S
		allStops.put("12340", "375aa194-0b68-4412-8dca-67ecdc65bbce"); // SCENIC DR S & 26 ST S
		allStops.put("12219", "357d7768-74d3-4ee5-865c-3645a5c661a0"); // STAFFORD DR S & 4 AVE S
		allStops.put("12218", "0f6411ae-1a5b-48a4-9ed4-32e57aba9ebd"); // STAFFORD DR S & 2 AVE S
		allStops.put("11192", "1c9e9a9b-ab3c-4b83-9610-e4e827bc8e49"); // 2 AVE A N & 10 ST N
		allStops.put("11230", "27354f17-ac0a-4111-990e-75d5f619bdfa"); // 2 AVE A N & 12 ST N
		allStops.put("11229", "f5951787-a0a4-424b-8f94-328d4fb67abb"); // 2 AVE A N & 13 ST N
		allStops.put("11144", "d0f460ac-1c43-410a-b599-7f2acd6cf907"); // 13 ST N & 2 AVE N
		allStops.put("11188", "9c880712-e51b-4f20-85d4-b8625f836d96"); // 2 AVE N & 13 ST N
		allStops.put("11187", "86e64128-af1a-475a-9c81-f2f3b16a417e"); // 2 AVE N & 16 ST N
		allStops.put("11185", "8f64f06e-6e99-4bc8-bf07-718edb5474aa"); // 2 AVE N & 18 ST N
		allStops.put("11184", "7b4ba0f8-c204-4ec3-8772-a338c0d1e838"); // 18 ST N & 3 AVE N
		allStops.put("11250", "3f23a31c-3016-49a5-97e2-03c0ef78afde"); // 18 ST N & 5 AVE N
		allStops.put("11181", "49d712c3-f6be-4c16-8fcb-323cf36d1dd8"); // 5 AVE N & 18 ST N
		allStops.put("11180", "e491e165-d676-4ba4-8e91-2c716bdad6e0"); // 5 AVE N & 21 ST A N
		allStops.put("11178", "4d6e7c06-c224-455c-b2b1-1e98f8c03887"); // 5 AVE N & 24 ST N
		allStops.put("11173", "bdf077a1-08fa-418b-ac5a-81327de151fc"); // 27 ST N & 7 AVE N
		allStops.put("11171", "bd4d8812-9d75-4937-8117-9975dc49e667"); // 27 ST N & 8 AVE N
		allStops.put("11170", "87e8d3e0-b0e5-448b-8541-d288a9276880"); // 27 ST N & 9 AVE N
		allStops.put("11168", "51e2c3d0-a5c3-400b-8c56-9919b3eae9c1"); // 9 AVE N & 23 ST N
		allStops.put("11150", "adb83a98-ad4f-4274-a047-3b80b7418c3f"); // 23 ST N & 13 AVE N
		allStops.put("11166", "e116ef18-4681-4a7e-bb2f-bee5c4f3f9a4"); // 14 AVE N & 23 ST N
		allStops.put("11163", "214a62b4-a30e-42d9-aca7-2ec26365fa78"); // 14 AVE N & 27 ST N
		allStops.put("11039", "e1aaf81d-dfdd-4b75-862d-f9b80eeb1c67"); // 11 ST N & 43 AVE N
		allStops.put("11161", "00f71859-c744-4a8e-bc40-29b9dfd5e988"); // 28 ST N & 18 AVE N
		allStops.put("11160", "9eb4be4c-1fa0-4fb7-a0d5-c278f29efaf2"); // MEADOWLARK BLVD N & PARKMEADOWS BLVD N
		allStops.put("11212", "4a6d0902-a2c4-44cf-95c0-938145d161c9"); // MEADOWLARK BLVD N & ROBIN RD N
		allStops.put("11211", "f22ab36f-d44a-493c-ba88-153bef337906"); // MEADOWLARK BLVD N & ORIOLE RD N
		allStops.put("11210", "c6edb783-f925-4375-a0fd-4edd8d71310d"); // MEADOWLARK BLVD N & 23 ST N
		allStops.put("11275", "95d3795d-575e-4aa0-9233-50be8211a7ad"); // 26 AVE N & BLUEFOX BLVD N
		allStops.put("11035", "a45adb11-2fde-46e1-98dd-39e173f89b31"); // 26 AVE N & ERMINEDALE BLVD N
		allStops.put("11053", "7eff7e75-f23f-401c-a6df-6e8df939a54f"); // North Terminal
		allStops.put("14014", "0e4ce7c0-b48f-43d9-a13d-e5fcdf547845"); // City Centre Terminal
		allStops.put("14022", "f82233bb-c1ed-4035-9357-4b4140b3ddab"); // 6 AVE S & 5 ST S
		allStops.put("13001", "ba91ec05-560d-46b7-9784-cc5b60a4566e"); // University Terminal
		allStops.put("14012", "91b5cca9-99ec-48c2-a3a1-9d40085249a1"); // 4 AVE S & 4 ST S
		allStops.put("14008", "36b4186d-1bee-473a-9c0a-aa3873f4c80a"); // 3 ST S & 2 AVE S
		allStops.put("14006", "94421eab-1c95-4520-b0d6-2174b29133ef"); // 1 AVE S & 5 ST S
		allStops.put("14004", "d471ec50-d4d7-4811-abde-0f1e4c400de8"); // 1 AVE S & 7 ST S
		allStops.put("11110", "715030a2-e98c-4682-9b27-8488cccf5d4f"); // STAFFORD DR N & 4 AVE N
		allStops.put("11108", "dc7f5fc9-8439-461b-9bf5-3da49d469ca5"); // 5 AVE N & 11 ST N
		allStops.put("11105", "2b73586b-80e1-42a6-9b59-30eb0386d2c1"); // 12 ST N & 5 AVE N
		allStops.put("11103", "71f6bdce-5815-4b51-b300-15950782a8c6"); // 12 ST N & 6 AVE N
		allStops.put("11225", "908b83f8-ebd3-4523-8dd9-172764b19d6e"); // 12 ST N & 7 AVE N
		allStops.put("11101", "b5a64619-9844-4e67-8541-ef4db22eb3ca"); // 12 ST N & 8 AVE N
		allStops.put("11100", "d9ef4e56-66ac-4b1d-8061-cb2312928531"); // 12 ST N & 9 AVE N
		allStops.put("11098", "6896931f-d4ce-4676-a4a1-c8eb46848443"); // 9 AVE N & 11 ST N
		allStops.put("11097", "f74cfa7c-e235-408c-82df-4f1b80e2d133"); // 9 AVE N & 9 ST N
		allStops.put("11093", "e0183fe0-3c24-4eb1-a77e-59f7e5293e2b"); // 6 ST N & 9 AVE N
		allStops.put("11091", "fecfa987-f050-4eae-acca-415be9789ee8"); // 6 ST N & 12 AVE N
		allStops.put("11089", "b4c2ee84-b193-44ce-9266-8579df7713ad"); // STAFFORD AVE N & 7 ST N
		allStops.put("11087", "eaa4834e-d346-4a88-95fe-cb89277fc24d"); // STAFFORD AVE N & 8 ST N
		allStops.put("11086", "bf3eead9-ec6e-4c53-8727-a0b9a480c383"); // STAFFORD AVE N & STAFFORD DR N
		allStops.put("11224", "275271b9-d783-40d3-a672-1d8ffa2cefd4"); // ST. EDWARD BLVD N & ST. GEORGE RD N
		allStops.put("11083", "0de8ab8f-9b4c-432d-8dbf-ed6081e10c42"); // St. EDWARD BLVD N & ST. DAVID RD N
		allStops.put("11081", "3d635893-b0c1-4dca-9ef5-5d761380ab9f"); // 18 AVE N & ERMINEDALE BLVD N
		allStops.put("11080", "bf0cd003-1b6b-4df2-8bb7-f9431589164a"); // 18 AVE N & 13 ST N
		allStops.put("11078", "c1955372-a727-498e-973e-0b18d36bb640"); // 18 AVE N & 16 ST N
		allStops.put("11076", "b97d2a7f-8358-432d-878d-58c7e0254ec8"); // 16 ST N & 15 AVE N
		allStops.put("11073", "363deffa-7706-4181-892d-5756fc1beb45"); // 18 ST N & 13 AVE N
		allStops.put("11072", "2be83a7f-0a1f-49dd-80ea-f4ef7f00ad0d"); // 13 AVE N & 19 ST N
		allStops.put("11070", "7f61f2e3-52f3-48e3-97a1-4d4b2b73f364"); // 20 ST N & 15 AVE N
		allStops.put("11068", "826f1164-57ff-44d2-898d-a0d28c82f21a"); // 23 ST N & 15 AVE N
		allStops.put("11065", "c1060102-80a4-4806-b010-8c08c16981bd"); // 18 AVE N & 20 ST N
		allStops.put("11063", "3b20ff3d-83d0-4ba5-bc47-d6edd2e65476"); // 18 ST N & 19 AVE N
		allStops.put("11062", "14d0dd47-da36-4d74-b5b1-18a29fb5cc76"); // 18 ST N & 23 AVE N
		allStops.put("11272", "482e9d11-2594-402a-8dcc-92c9d85b3b24"); // 23 AVE N & 17 ST N
		allStops.put("11127", "71af508d-2608-4e71-8879-8b41ba73f925"); // 13 ST N & 23 AVE N
		allStops.put("11126", "2964a4d8-1e27-4c1d-b7be-ef45ebbfd091"); // 13 ST N & 26 AVE N
		allStops.put("11277", "4090218e-4e6b-488e-af64-cc4e13603eb9"); // MILDRED DOBBS BLVD N & SCENIC DR N
		allStops.put("11001", "ec570e66-67ca-4069-b43e-a6ea1d0f2021"); // 41 AVE N & 12 ST N
		allStops.put("11002", "ecc682a9-e90b-4620-a98e-98aca3efd778"); // 11 ST N & 41 AVE N
		allStops.put("11003", "7a91f993-cd28-45b0-9eb8-0df1020e63e4"); // 11 ST N & 43 AVE N/42 AvE midway
		allStops.put("11032", "789abff6-d48c-4a93-bcd6-2171025f8d08"); // 11 ST N & 44 AVE N
		allStops.put("11004", "89c22891-b9c2-4cf7-bb4e-f22229f73de4"); // 44 AVE N & 12 ST N
		allStops.put("11006", "e7d5a139-298e-418e-9752-8f1b9faaaceb"); // 13 ST N & 41 AVE N
		allStops.put("13451", "f1e3f55a-8756-4e3b-adfe-4ae727c62d51"); // MICMAC BLVD W
		allStops.put("13090", "d137a339-3c3a-4d73-8a9c-cb900f61aeaf"); // MICMAC BLVD W & RED CROW BLVD W
		allStops.put("13029", "68d3d129-eaa4-463b-99fa-2727dd626134"); // RED CROW BLVD W & IROQUOIS CRES W
		allStops.put("13089", "681aa242-6444-4202-a82a-3602fb160fdd"); // JERRY POTTS BLVD W & RED CROW BLVD W
		allStops.put("13031", "2fd8d7fd-2083-4100-882e-da2be3bac790"); // JERRY POTTS BLVD & CHILCOTN RD W
		allStops.put("13032", "08dcb914-db7e-4ead-8cc1-a6447918dafc"); // JERRY POTTS BLVD W & KOOTENAY PL W
		allStops.put("13033", "ad1c4440-99e0-44e8-8a11-c71f368ed00c"); // JERRY POTTS BLVD W & NISKA PL W
		allStops.put("13034", "df8f4a62-f81f-4965-b63a-f8d2b86e1005"); // RED CROW BLVD W & JERRY POTTS BLVD W
		allStops.put("13035", "f39bfb86-d4a5-4419-a2d8-cec8cb65077f"); // RED CROW BLVD W & COWICHAN CRT W
		allStops.put("13036", "38de5a95-8417-4d05-b416-6628d3bb2619"); // RED CROW BLVD W & CHILCOTIN RD W
		allStops.put("13037", "3e0a1ee3-a089-4ecf-af86-1aa17902dcea"); // RED CROW BLVD W & BLACKFOOT BLVD
		allStops.put("13038", "48b182b9-349c-4db8-b337-eddfb3ff915a"); // RED CROW BLVD W & OJIBWA RD W
		allStops.put("13039", "eb057c9a-1f3b-4e71-8d38-5919519f195e"); // RED CROW BLVD W & JERRY POTTS BLVD W
		allStops.put("13040", "0c3dbcb9-7b65-4f7d-93e4-8c2d469e2234"); // JERRY POTTS BLVD & WHOOP UP DR
		allStops.put("13041", "048d062a-639e-42a9-b73a-e77b90f6aefd"); // MCMASTER BLVD W & COLUMBIA BLVD W
		allStops.put("13063", "dee1a94e-18e9-4e95-8f9b-c989d66aacd4"); // COLUMBIA BLVD W & PENSACOLA CRT W
		allStops.put("13064", "e3c5f516-41a7-4acd-867e-f6ec51be7024"); // COLUMBIA BLVD W & HARVARD CRES W
		allStops.put("13065", "3c585ea5-e154-4e1c-95be-82079c77a7a2"); // COLUMBIA BLVD W & PRINCETON CRES W
		allStops.put("13066", "ba95dbcb-6e38-4ab4-ad1e-a86e0e52e6c8"); // COLUMBIA BLVD W & TEMPLE BLVD W
		allStops.put("13067", "28d75db5-0df6-44bb-b939-07280b43fa04"); // COLUMBIA BLVD W & COLUMBIA BAY W
		allStops.put("13068", "dc3dfc9a-a7cd-4d5c-9f34-bf6b2a552f6b"); // COLUMBIA BLVD W & SIMON FRASER BLVD W
		allStops.put("13069", "d9b6e8dd-96f2-488d-9fe3-418630fef6ea"); // COLUMBIA BLVD W & SELKIRK RD W
		allStops.put("13070", "3d03e73d-9f1f-4fe1-bb17-cea7398348ab"); // COLUMBIA BLVD W & MCMASTER BLVD W
		allStops.put("13083", "0fceaf26-8831-4275-a5bc-fb4f02691ed4"); // MCGILL BLVD & MCMASTER BLVD W
		allStops.put("13085", "39d0e829-ba41-4fe3-9915-03662e4a086e"); // MCGILL BLVD W & QUEENS RD W
		allStops.put("13086", "89f7b3d2-c3b3-4f05-b6dc-22d0078d62cf"); // MCGILL BLVD W & LAVAL BLVD W
		allStops.put("13087", "d70276af-88ba-4070-a51e-04cf7fb9dc70"); // Mcgill Blvd & Dalhoursie Ct W
		allStops.put("13088", "551d8ec0-ebe8-40fb-9020-df8a104eab07"); // MCGILL BLVD W & UNIVERSITY DR W
		allStops.put("13005", "c38fef51-83d4-4cd3-8a84-7d8146af186b"); // University Terminal
		allStops.put("13004", "789c470f-3999-4db6-8317-1a12201cacc7"); // University Terminal
		allStops.put("13520", "52ca90d1-0749-4492-8d55-b739162fde63"); // Highlands Rd W & University
		allStops.put("13521", "24af378b-38f8-4adf-8150-360cd93282be"); // Highlands Blvd W & Highlands Rd W
		allStops.put("13522", "d86b174b-2729-4c87-923d-7e200bdc495a"); // Garry Dr W & Highlands Blvd W
		allStops.put("13523", "ba8eca32-e910-427b-9470-ec7beb304f0d"); // Garry Dr W
		allStops.put("13524", "4b43170a-c145-4985-8595-a2fa0ec4d3fc"); // Garry Station Prt W & Garry Dr W
		allStops.put("13525", "c4c68487-783d-4b61-99b8-0c341cc1fc5f"); // Aquitania Blvd W & Abitibi Rd W
		allStops.put("13526", "9b869336-b1cd-4a0e-94e1-b7a0dea1727c"); // Aquitania Blvd W & Lasalle Rd W
		allStops.put("13527", "d43964c9-19c7-4e99-86fd-17211977b81d"); // Jerry Potts Blvd W & Red Crow Blvd W
		allStops.put("13450", "dd7d1ef2-9ca9-4efd-a95f-1fb3caec156a"); // GARRY DR W & SQUAMISH BLVD W
		allStops.put("12131", "ff71d29d-c77a-45c3-8449-0163a61ae96b"); // 28 AVE S & 28 ST S
		allStops.put("12128", "a2f4c366-a3fb-4b82-8d48-15e6af750646"); // 28 AVE S & 32 ST S
		allStops.put("12301", "ef8ff01b-d4c0-415a-89e2-6c18f5e9bffe"); // COULEECREEK BLVD & COULEESPRINGS RD N
		allStops.put("12302", "61ccf15d-b0f6-4750-8919-99a3891c6a0a"); // COULEECREEK BLVD & COULEECREEK MNR N
		allStops.put("12199", "fe0ff7bb-793a-41be-8a49-919f0a62e45f"); // MAYOR MAGRATH DR & 26 AVE S
		allStops.put("12200", "abf019e1-e423-45fd-92cb-030b5202abf5"); // MAYOR MAGRATH DR S & 20 AVE S
		allStops.put("12201", "10d73891-7aa3-405d-830e-132479e4c22e"); // MAYOR MAGRATH DR S & 16 AVE S
		allStops.put("12202", "21870601-cc9f-431e-a343-76078a748a35"); // MAYOR MAGRATH DR S & 12 AVE S
		allStops.put("12203", "54781e8c-8a80-413f-8e74-f27dc79987fb"); // MAYOR MAGRATH DR S & SOUTH PARKSIDE DR S
		allStops.put("12204", "f42d838d-bcf5-4903-8328-bdc4d378351b"); // MAYOR MAGRATH DR S & 9 AVE S
		allStops.put("12205", "70b9ba73-9bc6-4bc0-8ecb-186ffcd14202"); // MAYOR MAGRATH DR & 6 AVE S
		allStops.put("12206", "6543eab1-8d9d-4ce9-9692-f0f98f85fb4e"); // MAYOR MAGRATH DR S & 5 AVE S
		allStops.put("11280", "2837fc3b-74bc-42c6-bb1b-8e56a13f0159"); // MAYOR MAGRATH DR & 2 AVE N
		allStops.put("11251", "83e40145-acc7-4652-911a-d7e204e76db0"); // 23 ST N & 5 AVE N
		allStops.put("11156", "593e2a14-7af1-4ab8-a21b-48d4f78a857d"); // 23 ST N & 7 AVE N
		allStops.put("11154", "c1ed1210-5cc7-4f8e-87b4-e726d0a2aedf"); // 23 ST N & 8 AVE N
		allStops.put("11148", "0dbf61b6-f596-48de-92a4-821c51d38d4c"); // 23 ST N & 23 AVE N
		allStops.put("11112", "3b5186b8-ed4e-4901-ae3c-22a45f0a8e28"); // STAFFORD DR N & STAFFORD RD N
		allStops.put("11114", "76d3ed00-080a-4044-907a-dc7c0e3ad86a"); // STAFFORD DR N & STAFFORD BLVD N
		allStops.put("11116", "459e559f-34e6-4493-b517-b28d697166bc"); // STAFFORD DR N & STAFFORD AVE N
		allStops.put("11118", "fe51c07a-c5ed-46e8-9ac8-e21dc681f9ca"); // STAFFORD DR N & 12 AVE N
		allStops.put("11050", "20656ad8-ef4a-4534-846e-4c4b5ae3804d"); // Scenic Dr & 3 Ave S
		allStops.put("14016", "c5e29d33-4999-42ec-9a4c-aaebae002ee9"); // City Centre Terminal
		allStops.put("13500", "cbecd5f5-aacc-4df0-9761-c3eaf3ff2839"); // UNIVERSITY DR W & MCGILL BLVD W
		allStops.put("13501", "11e6139c-8a2a-4b78-ab71-313c149f97b7"); // University Dr W
		allStops.put("13503", "8ebb8da5-a843-483c-8452-0c9b550d20f8"); // UNIVERSITY DR W & ROCKY MOUNTAIN BLVD WC
		allStops.put("13504", "823ff4e3-3200-409a-a24a-7f301ff6ef30"); // SUNRIDGE BLVD W
		allStops.put("13505", "58cf5e97-1320-4ff9-ac17-1c3d87da35f9"); // SUNRIDGE BLVD & SUNRIDGE CRES W
		allStops.put("13506", "db23e998-2b6e-46de-b669-0bb11b9c09aa"); // SUNRIDGE BLVD W
		allStops.put("13507", "3d04e494-193a-4e04-bbb9-c068854f9871"); // MT SUNDANCE RD W & MT SUNDIAL CRT W
		allStops.put("13508", "10a106ed-536c-4269-b66a-961fd5680a31"); // SUNRIDGE RD W & MT SUNDANCE MANOR W
		allStops.put("13509", "2a622372-74ef-49f1-bd00-888fe7ea132b"); // SUNRIDGE RD W
		allStops.put("13510", "10947c39-b416-43a2-9fe8-e09be7b41f26"); // SUNRIDGE RD W & SUNRIDGE BLVD W
		allStops.put("13511", "79fff424-f0d9-4f75-a9d5-8824831faf7b"); // UNIVERSITY DR W & GRAND RIVER BLVD W
		allStops.put("13518", "51b41937-cbdf-4267-b5a8-7b3093adab40"); // GRAND RIVER BLVD W & RIVERGREEN RD W
		allStops.put("13512", "0300d32e-6130-4182-b7ef-34d5e0c5638f"); // GRAND RIVER BLVD W & RIVERGREEN RD W
		allStops.put("13513", "d2c3747b-70ed-4ac5-97f5-289d64e0dedd"); // GRAND RIVER BLVD W & RIVERMILL LANDNG W
		allStops.put("13514", "e62e5541-8f92-4688-b4ef-d14670548cee"); // RIVERSTONE BLVD W
		allStops.put("13515", "604965ad-23c8-47f6-89fa-1b6f5089fc05"); // RIVERSTONE BLVD W
		allStops.put("13516", "4cf6aa3c-a577-46ad-98f4-fe6dd9db141e"); // RIVERSTONE BLVD W & RIVERGREEN RD W
		allStops.put("13517", "51a6f5a6-2151-49cb-9623-5adec5a6c1da"); // RIVERSTONE BLVD W
		allStops.put("13502", "9f68809a-fdc3-489f-a14e-b8d9b67ac109"); // UNIVERSITY & RIVERVIEW PKWY W
		allStops.put("13076", "1c867ece-4f08-45d7-b793-29ae5c329263"); // ROCKY MOUNTAIN BLVD W & UNIVERSITY DR W
		allStops.put("13077", "0b7b5551-a681-44fb-9707-c836cc1022cd"); // MT RUNDLE BLVD W & MT RUNDLE WAY W
		allStops.put("13078", "33544981-3a7e-49ad-a768-4b131e3ddecb"); // MT RUNDLE RD W & MT RUNDLE BLVD W
		allStops.put("13079", "75afd1dd-1d2c-4d6d-b309-0f51f1927a94"); // 284 Mt Blakiston Rd W
		allStops.put("13080", "99bcbf71-f3cc-4aae-8ca1-75c3009abc0a"); // MT BLAKISTON RD W & MT BLAKISTON PL W
		allStops.put("13081", "a43e723b-bd80-4a3c-8c27-a883941e3312"); // MT BLAKISTON RD W & MT BRAZEAU RD W
		allStops.put("13082", "894a25d5-28ec-4158-8f79-bb8fbe72aff9"); // MT BURKE BLVD W & MT BLAKISTON RD W
		allStops.put("13071", "0073565d-4972-46b3-b87b-1f03e35813d9"); // MACLEOD DR & MT BURKE BLVD W
		allStops.put("13072", "bb0d7494-9056-41ae-93ab-1ac8bd25940c"); // MACLEOD DR W & MT ALDERSON CRES W
		allStops.put("13532", "0175947a-8942-48bb-9b26-732f0b14d64f"); // University Dr W
		allStops.put("14031", "0d408335-f71f-4b6a-9c83-59ab06d8d20a"); // 3 AVE S & 7 ST S
		allStops.put("12250", "611f0a99-c6c6-44f7-9e9a-ad3350de7012"); // 3 AVE & STAFFORD DR S
		allStops.put("12251", "5e8df571-c4b3-472c-88e9-41fe9fdf39b1"); // 3 AVE S & 11 ST S
		allStops.put("12252", "24c8a4d8-8385-4264-9a3f-efcd92474255"); // 3 AVE S & 12 ST A S
		allStops.put("12342", "760d367c-285e-480b-ac9a-8e24563a4413"); // 13 ST S & 3 AVE S
		allStops.put("11143", "60a03758-cc67-4908-8b99-f7833d87a055"); // 13 ST N & 2 AVE N
		allStops.put("11140", "364a1b83-3141-436c-8d0c-bbad483036be"); // 13 ST N & 4 AVE N
		allStops.put("11279", "a240d3dd-0d54-40e9-9d81-7b0c26e22eb7"); // 5 AVE N & 13 ST N
		allStops.put("11196", "80f31597-7eb0-454a-90c5-cf88c1a326a0"); // 16 ST N & 5A AVE N
		allStops.put("11197", "c590fad8-eb13-4ebc-a9c0-60016fb7fe35"); // 16 ST N & 6 AVE N
		allStops.put("11203", "f4b2b62c-e195-46ad-8717-edeb74abccc8"); // 7 AVE N & 16 ST N
		allStops.put("11204", "b19814ba-3d34-462e-a2d3-651c92ef9bf4"); // 7 AVE N & 18 ST N
		allStops.put("11205", "57b1fd90-1281-4fe0-afd4-4ad6374370e7"); // 19 ST N & 7 AVE N
		allStops.put("11206", "53c8b715-727c-4225-9fa7-b287d1763448"); // 19 ST N & 8 A AVE N
		allStops.put("11207", "b8939d5a-baf3-44d8-93ba-edd2761c5b8a"); // 20 ST N & 10 AVE N
		allStops.put("11208", "a2a0cc6d-cead-4f2b-9ee1-a0e49b78168f"); // 12 AVE N & 19 ST N
		allStops.put("11202", "24ed059f-4efb-4f7b-9fc1-a8b589b8ed07"); // 13 AVE N & 15 A ST N
		allStops.put("11133", "736419f6-bdc0-420a-8691-5f8b601af664"); // 13 ST N & 13 AVE N
		allStops.put("11134", "fd0a48fd-aad8-4104-ae9f-fbfcfce77739"); // 13 ST N & 10 AVE N
		allStops.put("11137", "05744414-c298-4abb-9973-e8b360f9caee"); // 13 ST N & 8 AVE N
		allStops.put("11227", "f1e3bae6-c448-4e64-bf2e-18f1b87f9afe"); // 13 ST N & 6 AVE A N
		allStops.put("11141", "52304c48-3f02-4a43-b20d-e6a60d454dfe"); // 13 ST N & 4 AVE N
		allStops.put("11142", "ec113688-4989-4269-a8e7-78d0a498cc3f"); // 13 ST N & 3 AVE N
		allStops.put("12042", "a2962159-05c9-4729-9f06-c80be2c280d6"); // 13 ST S & 3 AVE S
		allStops.put("12043", "4d02b9d5-f529-47ff-850c-90246e667de3"); // 3 AVE S & 12A ST S
		allStops.put("12044", "1228c14b-9cfc-451a-b213-b1e16cddd959"); // 3 AVE S & 11 ST S
		allStops.put("12039", "43ebf2da-f4fb-4723-ac0c-a2a87723c4e0"); // 4 AVE S & 10 ST S
		allStops.put("14025", "b98ba4a4-adec-4189-898f-ae77bac846f6"); // 5 AVE S & STAFFORD DR S
		allStops.put("14034", "7e055730-824b-4b87-b507-924093354928"); // 4 Ave & 8 St S
		allStops.put("12038", "1438173d-a4c2-45f0-abab-0a5c22eafb21"); // 4 AVE S & 10 ST S
		allStops.put("12040", "fed1d91a-a140-46a5-a8f0-6a4c5d3aadc4"); // 11 ST S & 5 AVE S
		allStops.put("12022", "dc0c1b66-3b0f-4a36-8c89-179cbda93ae4"); // 4 AVE S & 12 A ST S
		allStops.put("12023", "22d9708d-0613-4a9c-8139-c516b6ac83c0"); // 4 AVE S & 13 ST S
		allStops.put("12025", "653bba16-e73a-45ff-9b84-15038d078a44"); // 3 AVE S & 16 ST S
		allStops.put("12026", "8114663d-f507-4db3-ae3b-979dd5817ac5"); // 3 AVE S & 17 AVE S
		allStops.put("12027", "f908a704-a1b8-48e4-b65f-adf1c7597557"); // 3 AVE S & 20 ST S
		allStops.put("12028", "2c2d6fe0-4384-4a58-8dd2-18a2cfe4b7f2"); // MAYOR MAGRATH DR S & 4 AVE S
		allStops.put("12029", "bb66df9b-e0d6-43f9-9043-a1a6620a5632"); // MAYOR MAGRATH DR S & 5 AVE S
		allStops.put("12020", "1150e35c-d044-450e-a753-2d13951aa3d6"); // 6 AVE S & 24 ST S
		allStops.put("12019", "d5fad709-7bba-494b-9d85-0cd7804014b1"); // 6 AVE S & 26 ST S
		allStops.put("12018", "e1efbf71-54dc-4c49-97fa-178d7afaa14f"); // 6 AVE S & 28 ST S
		allStops.put("12017", "7b5f1d0c-314e-4d75-b51f-c23688138ec9"); // 6 AVE S & 28B ST S
		allStops.put("12016", "25d39626-a687-43b2-9e13-dc0406c5b7e2"); // 6 AVE S & CORVETTE CRES S
		allStops.put("12015", "91bd4f3c-e68f-439d-af26-b75537c5ddba"); // 6 AVE S & DIEPPE BLVD
		allStops.put("12014", "f55d1d03-c1e3-4a0b-9f95-12e9fc0a6c32"); // 34 ST S & 6 AVE S
		allStops.put("12013", "67871c2a-6c57-47e2-9b56-1a8dba19a992"); // LEASIDE AVE S & 5 AVE S
		allStops.put("12012", "a1bf7427-2f17-479c-9c74-31390a86e91d"); // LEASIDE AVE S & ORTONA ST S
		allStops.put("12011", "3026d2e3-1052-4907-bb40-87b7672284dc"); // LEASIDE AVE S & 2 AVE S
		allStops.put("12010", "d29a5d96-3a4d-4c79-b9e6-898554d1d1a0"); // Transfer Point
		allStops.put("12330", "213c7549-0590-4d65-8532-ed347e529b04"); // 1 AVE S & 32 ST S
		allStops.put("12331", "d0862ea4-d355-4ff9-8cc9-1bf9522d1e19"); // 34 ST S & 3 AVE S
		allStops.put("12303", "4d0056aa-e7f9-4588-9b11-81829d0bbb9c"); // W T HILL BLVD S & 4 AVE S
		allStops.put("12332", "0f860b26-70b9-4793-9579-7948063a1dc2"); // W T HILL BLVD S & 40 ST S
		allStops.put("12002", "861f4d10-2b24-48c2-a289-353f3e79a6b4"); // 40 ST S & 4 AVE S
		allStops.put("12333", "c78fc119-23b8-45ae-a59f-518a26087562"); // 4 AVE S & 41 ST S
		allStops.put("MESC0", "40273f90-d38a-483f-a859-f3abaacb58e8"); // 43 ST N & 5 AVE N
		allStops.put("11265", "6f8aa2f0-693b-446d-acdb-78a1ca95ed6c"); // 9 AVE N & 43 ST N
		allStops.put("11266", "44e5bf6c-2cc5-4ef9-a6f9-cc40c4348fb9"); // 9 AVE N & 39 ST N
		allStops.put("11267", "0fc50135-44ea-4898-a839-dc216926e715"); // 39 ST N & 14 AVE N
		allStops.put("11271", "c00c77a1-f9ee-4114-8f81-a191568b723e"); // 14 AVE N & 39 ST N
		allStops.put("11268", "99b8ee1c-95e0-47ec-86c5-456e7b830aa6"); // 36 ST N & 14 AVE N
		allStops.put("11269", "f9d1dbdb-9f7b-4585-b0a8-b388fd3522f7"); // 36 ST N & 12 AVE N
		allStops.put("11270", "7a20b2df-c100-48c5-8c9d-7ca23b6cf2f3"); // 36 ST N & 9 AVE N
		allStops.put("11215", "4cb4264d-8d12-43e9-9554-d47d954a2267"); // 6 AVE N & 36 ST N
		allStops.put("11235", "1d70b76f-37c8-42f7-9a5c-87c072a4e2b8"); // 39 ST N & 6 AVE N
		allStops.put("11236", "1c810caa-34be-4a28-9a4a-092b7c6ab5c9"); // 39 ST N & 5 AVE N
		allStops.put("11216", "7d5a956b-6881-445d-a322-1906622499db"); // 36 ST N & 5 AVE N
		allStops.put("11217", "c22181a3-e1f9-4557-8cbe-b582c20546af"); // 36 ST N & 2 AVE N
		allStops.put("11048", "eab6dcd7-83d8-4c54-9398-8ef180dd9248"); // 2 AVE N & 42 ST N
		allStops.put("11054", "c30694fe-0d8d-46cd-a8d1-8e1d0bb2470a"); // 36 ST S & CROWSNEST HWY
		allStops.put("12009", "02530e64-4b27-41d6-bafa-d2baea0a09a0"); // 28 ST S & 3 AVE S
		allStops.put("12008", "7204bfcf-3080-4f17-b4b1-2eb4ffb9d91a"); // 28 ST S & 4 AVE S
		allStops.put("12007", "7c0b974e-93f0-4b5e-ae55-795092433b01"); // 4 AVE S & 25 ST S
		allStops.put("12006", "62d4418f-4e8a-4f05-9e47-20755e130930"); // 23 ST S & 4 ST S
		allStops.put("12031", "90353237-6125-405b-aa29-5cc868ed4c65"); // 6 AVE S & 21 ST S
		allStops.put("12032", "d5debd02-9e89-4356-a8cc-0466dc6a0bea"); // 6 AVE S & 18 ST S
		allStops.put("12034", "f59e395a-877e-4624-b779-1f720c01ec4d"); // 6 AVE S & 15 ST S
		allStops.put("MESC1", "4ed4b1e1-d0d8-4d85-b3f3-9cbbd7cad122"); // 6 AVE & 13 ST S
		allStops.put("12035", "7f1b6611-86af-41c7-ba2d-b0b1c5bb55f6"); // 6 Ave & 13 St S
		allStops.put("12036", "c3fd5a6f-c46b-4ad2-92f7-021563b37e49"); // 6 AVE S & 12 ST S
		allStops.put("12037", "caafc7e7-9b30-43be-bf08-64351d027b64"); // 6 AVE S & 11 ST S
		allStops.put("14026", "f9df41e9-6a95-428d-8b84-7fdca4e99efe"); // 6 AVE S & 8 ST S
		allStops.put("11037", "31afcbeb-2269-43e8-8a5f-2afe7d3fa08a"); // 26 AVE N & ERMINDALE BLVD N
		allStops.put("11031", "34e85553-3943-42be-b535-561ae7720172"); // 26 AVE N & ERMINEDALE BLVD N
		allStops.put("11276", "ff70102b-23cf-41a8-a704-bebaa517b287"); // 26 AVE N & 23 ST N
		allStops.put("11209", "06c1a8a0-ab5f-4a1b-8e13-c27d83b21397"); // MEADOWLARK BLVD N & 23 ST N
		allStops.put("11159", "05abd12e-c355-41fa-9d73-30deeb03a8e9"); // MEADOWLARK BLVD N & 15 ST S
		allStops.put("11252", "7faff261-2c14-455a-9438-6fcf0fbe656c"); // MEADOWLARK BLVD N & PARK MEADOWS BLVD
		allStops.put("11162", "554d1d85-aa14-45a4-8d95-fb4a25e16b2e"); // 14 AVE N & 28 ST N
		allStops.put("11164", "9d056ac6-2b30-4741-8234-4efe41bc0e60"); // 14 AVE N & 26 ST N
		allStops.put("11165", "0ad362d9-cc45-460e-898c-81c4105a775b"); // 14 AVE N & 23 ST N
		allStops.put("11149", "4252811e-16e6-4d43-9e15-a89995f851f6"); // 23 ST N & 13 AVE N
		allStops.put("11151", "2732dd52-1e0b-43c8-aec2-26d6b9416111"); // 23 ST N & 10 AVE N
		allStops.put("11167", "e843780d-3b6e-46f2-a283-8aefe73058f2"); // 9 AVE N & 23 ST N
		allStops.put("11169", "9a1506e7-ef40-462a-8cd6-75b6a94ac50f"); // 27 ST N & 9 AVE N
		allStops.put("11172", "c980cca8-c5c4-4de8-bab4-36149c868a51"); // 27 ST N & 7 AVE N
		allStops.put("11174", "2e099dd3-5a67-495e-8b4a-49962c7a2380"); // 6 AVE N & 27 ST N
		allStops.put("11176", "0924ccdd-786c-440b-ba6f-2d6158ec9fa2"); // 5 AVE N @ 28 ST N
		allStops.put("11177", "901a691a-6810-4c06-9abd-5e9b00d66209"); // MAYOR MAGRATH DR S & 5 AVE N @ 26 ST N
		allStops.put("11179", "fa44a314-11ab-4e14-b083-318d43b2379b"); // 5 AVE N & 23 ST N
		allStops.put("11218", "55b4fae1-07d3-431d-8ad5-2326cb3cfe47"); // 5 AVE N & 21 ST N
		allStops.put("11182", "528e8b53-cd4c-4ba2-9e8a-1a5f4f3a0e6e"); // 18 ST N & 5 AVE N
		allStops.put("11183", "fa36e2e8-06ee-4b5a-8540-cb5169d9a24c"); // 18 ST N & 3 AVE N
		allStops.put("11219", "40db75f4-a369-4606-9e1f-42817087359b"); // 18 ST N & 2A AVE N
		allStops.put("11186", "c8fc35ab-4239-4f60-8a80-1366c05654f0"); // 2 AVE S & 16 ST N
		allStops.put("11189", "27725cb6-2809-4407-8f0b-9f2e91e04462"); // 2 AVE N & 13 ST N
		allStops.put("11190", "dc90c1a1-1e7c-40da-937f-07be5a0a1e85"); // 2 AVE A N & 13 ST N
		allStops.put("11191", "9589e7bb-931f-46c1-b168-c7d8939c9bf4"); // 2 AVE A N & 12 ST N
		allStops.put("11193", "28f0fcdc-26db-4319-b61c-6331d3afaebb"); // 2 AVE A N & 10 ST N
		allStops.put("14009", "2fe14c71-a83f-44e7-bb26-9b63e4244a1b"); // STAFFORD DR S & 2 AVE S
		allStops.put("14010", "98922414-3bb8-45f6-9c86-4db53f60bb45"); // 4 AVE S & 8 ST S
		allStops.put("12132", "edd3c5ba-a12d-4551-beae-738b49dbb7e5"); // TUDOR BLVD S & TUDOR CRES S
		allStops.put("12133", "c3996485-3c3c-4cc6-8e56-25d1c3a04571"); // TUDOR BLVD S & TUDOR CRES S
		allStops.put("12134", "63d7c57f-e7e0-4848-a32b-ae2373049cfa"); // TUDOR CRES S & TUDOR CRT S
		allStops.put("12135", "1e2634f5-1561-4f5d-9ee3-6fa340595e74"); // 20 ST S & SCENIC DR S
		allStops.put("12136", "0d68ad33-6a37-4456-9ae2-0f063995e676"); // 20 ST S & 23 AVE S
		allStops.put("12138", "25911f11-7932-42a3-a5d0-a7404aa85fe7"); // 20 ST S & 20 AVE S
		allStops.put("12141", "3b58aaea-15f1-42b8-bf79-42237ba1c420"); // 20 ST S & 19 ave s
		allStops.put("12142", "1ac775d7-4e75-442b-9087-cd74ca00b6fe"); // 20 ST S & 16 AVE S
		allStops.put("12150", "4c481d5d-6b7e-4c79-b037-275d2f596ed6"); // 16 AVE S & 18 ST S
		allStops.put("12151", "afb1e4b4-1d03-4902-a265-43e804d264d4"); // 16 AVE S & 15 ST S
		allStops.put("12152", "72a90ffc-0671-40df-beb5-e5db9508481c"); // 13 ST S & 15 AVE S
		allStops.put("12153", "c66d8deb-dcc6-4125-9c78-8c84459f70a6"); // 12 AVE S & 13 ST S
		allStops.put("12154", "5abe5648-425e-46f1-8c22-cb02590f5093"); // 12 AVE S & 15 ST S
		allStops.put("12158", "5141388b-2649-46d3-bd0d-1180574c63a0"); // 12 AVE S & 16 ST S
		allStops.put("12159", "a5859a88-3918-47c0-b76c-18d1baf0300f"); // 12 AVE S & 18 ST S
		allStops.put("12160", "47a9a04b-8b09-41bd-ac83-7028d1d74a5f"); // 20 ST S & 12 AVE S
		allStops.put("12161", "e88d4225-d2dd-44e9-b13e-7d6208f89295"); // 20 ST S & 10 AVE S
		allStops.put("12164", "af35fd5b-6a80-48b0-8a5e-1561f46da31e"); // 10 AVE S & 18 ST S
		allStops.put("12167", "040a2f67-4117-4a9c-b0a1-a6ed594c5e4d"); // 10 AVE S & 14 ST S
		allStops.put("12169", "4ff1e366-9b90-474f-9f42-a8344aefb1f8"); // 10 AVE S & 12B ST S
		allStops.put("12170", "60547572-9c89-4952-9e03-a575d8ee896c"); // 10 AVE S & 11 ST S
		allStops.put("12172", "a35051f7-b360-4588-a8e5-6fa7b6547a5a"); // 10 AVE S & 7 A ST S
		allStops.put("14032", "85216ab8-11b4-4ffd-bd46-f6ff56b1e926"); // 4 ST S & SCENIC DRIVE
		allStops.put("12177", "8beb10c9-e32f-4075-8c7b-575e8cb52603"); // 7 A AVE S & 5 ST S
		allStops.put("12178", "89dbf14e-79ec-4777-b184-4a9d1e7d2e91"); // 5 ST S & 6 AVE S
		allStops.put("14020", "bbc25763-896d-4bda-8b11-291be229864e"); // SCENIC DR S & 5 AVE S
		allStops.put("11036", "d0f2e10c-125a-4b87-9e92-a487632e8d08"); // 26 ST N & 5A AVE
		allStops.put("11274", "a06334e6-e65b-4771-be03-59698e0cfd1a"); // 26 AVE N & 23 ST N
		allStops.put("11022", "0668cbe4-7072-49a6-9533-3d9b0aeea7a2"); // 28 ST N & 26 AVE N
		allStops.put("11023", "ac3814fc-3ebb-4667-bea7-8ed3adbeb55f"); // KODIAK BLVD N & KODIAK PL N
		allStops.put("11024", "fea0bfb2-9b1e-4138-96cb-ce04943be284"); // KODIAK BLVD N & GRIZZLY TERR N
		allStops.put("11025", "7432e58d-b47b-4469-a509-89b643d93905"); // BLUEFOX BLVD N & KODIAK BLVD N
		allStops.put("11273", "92cc0ab5-3cbd-4dbc-9043-452fd05a1081"); // BLUEFOX BLVD N & ERMINERUN PL N
		allStops.put("11028", "e30f5399-1079-458c-a747-1e2ff99c2649"); // ERMINEDALE BLVD N & BLUEFOX BLVD N
		allStops.put("11029", "d1453672-c70c-4056-a0ed-9365978174da"); // ERMINEDALE BLVD N & UPLANDS BLVD N
		allStops.put("11011", "a54c3d89-9084-479f-a225-0cae6adb1509"); // UPLANDS BLVD N & COUGAR CRES N
		allStops.put("11034", "2e793f74-6c50-455b-bd6b-d9f24d7f3413"); // UPLANDS BLVD N & BLUEFOX RD N
		allStops.put("12105", "29eec136-1fa8-4ed9-bf00-0c7b6c271412"); // COLLEGE DR S & 27 AVE S
		allStops.put("12339", "ad427110-dfa1-4b46-9866-1776ae53240e"); // SCENIC DR S & 26 ST S
		allStops.put("12094", "395f07a9-84de-4322-8f2f-fcd911a311c5"); // 20 AVE S & LAKESIDE RD S
		allStops.put("12095", "190e106b-4220-4d7f-9767-c0a9bc59918b"); // 32 ST S & 23 AVE S
		allStops.put("12096", "af416302-7530-4ce8-b58b-287036d27c85"); // 23 AVE S & 35 ST S
		allStops.put("12097", "44646e6f-ca73-463e-a66a-91cfa60e29ab"); // 23 AVE S & 38 ST S
		allStops.put("12098", "8bd1eaed-a150-4dc9-ae46-13b854417aa0"); // 40 ST S & 21 AVE S
		allStops.put("12088", "e941d4cd-aff6-4f65-8ce3-aa61664d675e"); // CEDAR RD S & 20 AVE S
		allStops.put("12086", "95baa635-87a9-4027-9ed8-2eecc0a7e03d"); // CEDAR RD S & FORESTRY AVE
		allStops.put("12084", "ad0e28d3-7e2e-4148-851b-d52976d394c7"); // FORESTRY AVE S & ASPEN PL S
		allStops.put("12082", "7c991db7-e9f1-405d-8da3-0552bfcc22f3"); // FORESTRY AVE S & ASHGROVE RD S
		allStops.put("12080", "f537c592-0165-4623-9ec3-b8fa91739c5a"); // LAKEMOUNT BLVD S & SYLVAN RD S
		allStops.put("12075", "f047fcaa-7d55-423b-b7d4-829024396ae0"); // GREAT LAKES RD S & SOUTH PARKSIDE S
		allStops.put("12072", "dc680e9e-30ad-4620-b613-5bbf2f220c4b"); // HENDERSON LAKE BLVD S & SOUTH PARKSIDE DR S
		allStops.put("12069", "0d0c93c0-213f-4d06-9949-dbbcc3903e16"); // HENDERSON LAKE BLVD S & 31 AVE S
		allStops.put("12065", "2ffa0bc5-ddf7-47df-8534-1580506801dd"); // 12 AVE S & SOUTH PARKSIDE DR S
		allStops.put("12066", "2e4af888-65af-4a5a-a2e8-a929ae1384ed"); // 12 AVE S & 28 A ST S
		allStops.put("12067", "c7d51aa2-91c0-4b49-b8a1-b325b76a27d9"); // 12 AVE S & 28 ST S
		allStops.put("12061", "c6cfffce-f81e-4a4c-86a5-eaef2324aa24"); // 9 AVE S & MAYOR MAGRATH DR S
		allStops.put("12059", "8e79e72a-c0f3-4dfe-b391-7682c2209cfc"); // 9 AVE S & 21 ST S
		allStops.put("12056", "30004e57-3c16-4cbf-99ba-a4dc0cb45a17"); // 9 AVE S & 19 ST S
		allStops.put("12053", "b9fe17fa-ec53-4ffa-8a4c-48c6559d4d7e"); // 9 AVE S & 7 AVE S
		allStops.put("12052", "3417a77f-50e0-4cfe-b127-85737ab9d85a"); // 9 AVE S & 15 ST S
		allStops.put("12050", "694d1fa5-e964-487a-bcdb-47016733e283"); // 9 AVE S & 13 ST S
		allStops.put("12189", "15a5c0e9-9745-4bb4-a9da-f0e929951c24"); // 13 ST S & 9 AVE S
		allStops.put("12190", "9da189ac-587e-4581-bfda-1cbc4c239bfd"); // 13 ST S & 7 AVE S
		allStops.put("12191", "0d11caf7-cd2e-4579-a6ad-05265a146784"); // 13 ST S & 6 AVE S
		allStops.put("12192", "d3fcf6ce-aeab-425c-86a1-6e2e42496f9d"); // 13 ST S & 4 AVE S
		allStops.put("12021", "ed4d594c-c694-4b68-9dd5-bc1f978b4886"); // 4 AVE S & 12A ST S
		allStops.put("14021", "9215db1b-537b-425f-9b4a-6c89191772e9"); // City Centre Terminal
		allStops.put("14002", "ed69c355-3baf-4e04-bd34-bb5e53b2f74c"); // SCENIC DR S & 2 AVE S
		allStops.put("14033", "decb09ca-23f2-4e1e-8bc8-86d9f631dedd"); // SCENIC DR & 1 AVE S
		allStops.put("11121", "3a7f8bf8-6ab9-4616-9282-3cafd1a3edf4"); // STAFFORD DR N & 9 AVE N
		allStops.put("11119", "52203040-cb92-4102-9840-509e314a41fd"); // STAFFORD DR N & 13 AVE N
		allStops.put("11115", "eff58397-a410-4e56-8c76-66a7cad2e6d4"); // STAFFORD DR N & STAFFORD BLVD N
		allStops.put("11113", "e0b306d9-e2d1-443a-8595-f923e71b86b1"); // STAFFORD DR N & STAFFORD BLVD N
		allStops.put("11111", "0aa7c7bb-7e04-4357-a608-1dc62e39181b"); // STAFFORD DR N & ST. JAMES BLVD N
		allStops.put("11147", "73c3a6a7-2af2-4add-9128-74ab0f378c1a"); // 23 ST N & 23 AVE N
		allStops.put("11153", "c54594ad-c1ad-40b9-a782-0461242d390e"); // 23 ST N & 8 AVE N
		allStops.put("11155", "5b4b71cd-99fe-4703-b5eb-44b7514212b0"); // 23 ST N & 7 AVE N
		allStops.put("11278", "f57fe9d2-baaa-4a86-b353-9e3b1c45426a"); // 23 ST N & 5 AVE N
		allStops.put("11157", "b808f07e-9d43-4d06-a17f-7de5f753ef79"); // 23 ST N & 2 AVE N
		allStops.put("11158", "4939cde7-966d-4922-8028-a84344297cf2"); // COLLEGE DR S & 28 AVE S
		allStops.put("12207", "f2fefa9d-1986-4af4-bedc-8c9120427872"); // MAYOR MAGRATH DR S & 7 AVE S
		allStops.put("12213", "383d5dc3-3618-4d56-a132-37b3f76deef0"); // MAYOR MAGRATH DR S & 16 AVE S
		allStops.put("12103", "87a2ea13-99d5-4124-ba06-1dcf4c429ce8"); // SCENIC DR S & 24 ST S
		allStops.put("12182", "f7fe8056-9f25-4e67-a4c1-762ceffa608d"); // 13 ST S & 15 AVE S
		allStops.put("12180", "0d26a222-3245-42f1-852b-b8441c341fec"); // SCENIC DR S & 4 ST S
		allStops.put("14019", "0d06cfac-2678-4c44-b999-62b893908253"); // 5 AVE S & SCENIC DR S
		allStops.put("14017", "82afa80c-961b-4fee-81ff-74a3f81b0303"); // 5 AVE & 4 ST S
		allStops.put("14018", "fff6d7e9-8181-47ea-ad4c-b90c2f88e8ae"); // 5 AVE S & SCENIC DR S
		allStops.put("12181", "c1f8c384-862d-42d3-bec4-cbfd8c701e46"); // Radiology Associates - Scenic Dr S
		allStops.put("12183", "a57151b2-9070-4e95-b1b7-951271e498da"); // SCENIC DR S & 16 AVE S
		allStops.put("12184", "44cf2149-5505-43fc-a8e4-45b7d4329127"); // SCENIC DR S & 15 ST S
		allStops.put("12185", "d2ac30a6-36c0-49f0-af27-a0bf92125e83"); // SCENIC DR S & CHINOOK HEIGHTS
		allStops.put("12104", "f02b20a5-f12b-4932-8d6a-7d61571bda30"); // COLLEGE DR & 28 AVE S
		allStops.put("13046", "aa7bb641-d385-4535-95d3-739d1d51d34f"); // COLUMBIA BLVD W & BERKELEY PL W
		allStops.put("13047", "c64c2b0d-64bd-4381-9a43-e3ca734b35ff"); // COLUMBIA BLVD W & LAVAL BLVD W
		allStops.put("13048", "f60548d2-94a6-4911-842a-928bf36ca5b7"); // COLUMBIA BLVD W & MODESTO RD W
		allStops.put("13049", "31b7e393-7465-4755-a451-5c56e672f48a"); // COLUMBIA BLVD W & LAFFAYETT BLVD W
		allStops.put("13300", "4b5158dc-0971-4b08-a8a8-237c54723a47"); // WHOOP UP DR W & JERRY POTTS BLVD W
		allStops.put("13312", "7bdc0fe3-3941-467a-8c95-39b0e69854e1"); // WHOOP UP DRIVE W & BLACKFOOT RD W
		allStops.put("13528", "6f404b5d-63c0-4d5a-8c7a-7d4a85560dbc"); // Aquitania Blvd W & Carinthia Rd W
		allStops.put("13311", "13861588-dd80-40d3-8edc-1217e62a0860"); // BRITANNIA BLVD W & AQUITANIA BLVD W
		allStops.put("13529", "db3d0abc-afec-4ef1-9dc8-072bafaa3f60"); // Mauretania Rd W & Britannia Blvd W
		allStops.put("13302", "b0b7f29a-02af-43a3-a79a-91971731de6b"); // SILKSTONE RD W & SILKSTONE BAY W
		allStops.put("13303", "0ce6417f-60f2-46c5-9563-32b8b13a8c88"); // SILKSTONE RD W & SILKSTONE CRES W
		allStops.put("MESC2", "5af2899a-361c-458d-85e8-684dce3eb1e5"); // Silkstone Rd W & Silkstone Close W
		allStops.put("13304", "cbcbf8e3-6915-4480-8b7a-6945ce51165f"); // COALBANKS LINK W
		allStops.put("13305", "a1d074fd-ef17-4343-abfc-d205745f0d76"); // COALBANKS BLVD W & COALBANKS LINK W
		allStops.put("13306", "853f0f9b-255b-4380-8714-c0cb15782db3"); // COALBANKS BLVD W
		allStops.put("13307", "0cf6287b-fa6f-4f32-bb8a-842bdf403207"); // COALBANKS BLVD W & TWINRIVER LINK W
		allStops.put("13308", "8ac025d7-c70f-4e18-b47e-feb8380d0b7d"); // KEYSTONE TERR W
		allStops.put("13309", "d48a533e-3477-406e-9e17-aee12b030e92"); // KEYSTONE TERR W
		allStops.put("13310", "aae659b1-bed3-4f1e-9c13-c7f51d21c57a"); // SILKSTONE GATE W & SILKSTONE RD W
		allStops.put("13301", "28e20cdc-7dd6-4e4c-ac94-9f4aa50fa2d3"); // WHOOP UP DR W & BLACKFOOT RD W
		allStops.put("13313", "e0cc87f3-f33e-49a6-a50b-951259bc5f8b"); // WHOOP UP DR W & MCMASTER BLVD W
		allStops.put("13042", "7f23ee2d-6e1d-4ce6-9f0d-3bbf56cebd7d"); // COLUMBIA BLVD W & lafayette blvd w
		allStops.put("13043", "4f90f142-fa37-4df3-8658-9b7ff57eb745"); // COLUMBIA BLVD W & MODESTO RD W
		allStops.put("13044", "b80eb318-3be6-4ab5-994a-27d77baa7603"); // COLUMBIA BLVD W & LAVAL BLVD W
		allStops.put("13045", "0429e448-796d-4597-9167-678da6e0f491"); // COLUMBIA BLVD W & BERKELEY PL W
		allStops.put("11007", "204c14b0-a847-4ed9-bfe2-1fe3dc4439f3"); // MILDRED DOBBS BLVD N & JESSIE ROBINSON CL N
		allStops.put("11008", "fc7f6454-6ba3-4492-864c-0c155c2cd9e0"); // EDITH EMMA COE RD N & MILDRED DOBBS BLVD N
		allStops.put("11009", "8d294624-7836-4390-9fde-44541e545d0f"); // EDITH EMMA COE RD N & EDITH EMMA COE RD N
		allStops.put("11010", "0ae0af10-7182-4ab1-82e5-9ea03970752d"); // LETTICE PERRY RD N & DOROTHY GENTELMEN CRES N
		allStops.put("11033", "51e32503-1ff9-462f-b6de-fef768b20e62"); // LETTICE PERRY RD N & DOROTHY GENTLEMAN CRES N
		allStops.put("11005", "9d29b9bf-5b72-4d5a-8070-c51dd4fe550b"); // 13 ST N & 43 AVE N
		allStops.put("11012", "24d441b9-5426-4306-9401-8a89bec38189"); // UPLANDS BLVD N & ERMINEDALE BLVD N
		allStops.put("11013", "d2598fbe-7d58-4921-97f1-9b859ab6cbe5"); // UPLANDS BLVD N & KODIAK BLVD N
		allStops.put("11254", "8d82087d-1e78-4e2f-8e6d-65c606816cd6"); // UPLANDS BLVD N & KODIAK BLVD N
		allStops.put("11255", "c21c3429-94d8-4009-a436-89c4680c5fda"); // UPLANDS BLVD N & BLACKWOLF WAY N
		allStops.put("11256", "acad4350-6872-4347-b8cf-c20debb6dadd"); // BLACKWOLF BLVD N & UPLANDS BLVD N
		allStops.put("11257", "88beddc1-3664-4bc5-9b10-7a29896632fd"); // LYNX RD N & BLACKWOLF BLVD N
		allStops.put("11258", "578597c0-862e-4696-8055-72b3980fb377"); // LYNX RD N & TIMBERWOLF WAY N
		allStops.put("11259", "15de00f0-1d08-4b4f-a9df-1ad6739a2672"); // LYNX RD N & KODIAK BLVD N
		allStops.put("11016", "aab03cba-8946-4aec-b80c-45c09b54cc9e"); // KODIAK BLVD N & LYNX CRES N
		allStops.put("11017", "96153d39-35b3-4990-973d-e553beb1d99b"); // KODIAK BLVD N & GRIZZLY RD N
		allStops.put("11018", "1b9c77b1-8371-472b-b9b3-c5ea289631fe"); // GRIZZLY RD N & 32 ST N
		allStops.put("11260", "340dd6dc-e4fe-411a-b542-e0c5f70d444a"); // 32 ST N & 32 AVE N
		allStops.put("11261", "14a9b1c9-a86c-43f7-88dd-882756cebdb1"); // GIFFEN RD N & 32 ST N
		allStops.put("11262", "e4d9915b-7ed9-4526-8825-9b7298eb5a1d"); // GIFFEN RD N & 36 ST N
		allStops.put("11263", "e9f2bd42-de1f-4398-a16b-ae0dbd5a8148"); // 36 ST N & 32 AVE N
		allStops.put("11264", "31d06e49-e672-491e-a716-6cac1538d6fa"); // 36 ST N & 30 AVE N
		allStops.put("11020", "1f9d3bd5-597a-4e8e-8cc7-5b56dc4c997b"); // 26 AVE N & 36 ST N
		allStops.put("11021", "c1763817-d551-43c8-8f50-a699061c5ac0"); // 26 AVE N & WALMART
		allStops.put("11026", "b5a1ac1e-c174-466b-b2bd-3f9a86272239"); // ERMINEDALE BLVD N & BLUEFOX BLVD N
		allStops.put("MESC3", "611a0592-fab9-4ccc-80f0-a78aaa90c044"); // 6 AVE & 13 ST S

		ALL_STOPS = allStops;
	}
}
