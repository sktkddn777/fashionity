// 설치했던 fontawesome-svg-core와 vue-fontawesome
import { library } from "@fortawesome/fontawesome-svg-core";

// 설치했던 아이콘파일에서 해당 아이콘만 불러옵니다.
import { fas } from "@fortawesome/free-solid-svg-icons";
import { far } from "@fortawesome/free-regular-svg-icons";
import { fab } from "@fortawesome/free-brands-svg-icons";

import { faTrashAlt } from "@fortawesome/free-regular-svg-icons";

export function loadFonts() {
  // 불러온 아이콘을 라이브러리에 담습니다.
  library.add(faTrashAlt);
  library.add(fas, far, fab);
}
