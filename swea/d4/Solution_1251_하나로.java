package swea.d4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * [S/W 문제해결 응용] 4일차 - 하나로
 */
public class Solution_1251_하나로 {
	static double Ans;

	static int[] p; // 부모를 저장할 배열
	static int[] rank; // 랭크를 저장할 배열, 높이나 레벨이라고 하기엔 애매함

	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int C = Integer.parseInt(br.readLine().trim()); // Test Case 수

		for (int tc = 1; tc <= C; tc++) {
			Ans = 0;
			int N = Integer.parseInt(br.readLine().trim()); // 섬의 수

			StringTokenizer st_x = new StringTokenizer(br.readLine(), " ");
			StringTokenizer st_y = new StringTokenizer(br.readLine(), " ");

			Node[] nd = new Node[N];
			for (int i = 0; i < N; i++) {
				int x = Integer.parseInt(st_x.nextToken());
				int y = Integer.parseInt(st_y.nextToken());

				nd[i] = new Node(i, x, y);
			}

			double duty = Double.parseDouble(br.readLine());
			
			int E = (N * (N - 1)) / 2; // 총 간선의 수

			p = new int[N + 1];
			rank = new int[N + 1];

			Edge[] ed = new Edge[E];
			int index = 0;

			for (int i = 0; i < nd.length; i++) {
				for (int j = 0; j < N; j++) {
					if (i == j) {
						break;
					}

					double val = (double)(duty
							* (Math.pow(Math.abs(nd[i].x - nd[j].x), 2) + Math.pow(Math.abs(nd[i].y - nd[j].y), 2)));

					ed[index++] = new Edge(nd[i].index, nd[j].index, val);
				}
			}

			Arrays.sort(ed);

			for (int i = 0; i < p.length; i++) {
				makeSet(i);
			}

			for (int i = 0; i < ed.length; i++) { // 가중치가 낮은 간선부터 선택하기
				Edge e = ed[i];

				if (findSet(e.a) != findSet(e.b)) { // 서로 다른 집합일 경우만 합치기
					unionSet(e.a, e.b);
					Ans += e.val;
				}
			}
			System.out.println("#" + tc + " " + Math.round(Ans));
		}
	}

	/** 멤버 x를 포함하는 새로운 집합을 생성 */
	public static void makeSet(int x) {
		p[x] = x; // 부모 : 자신의 index로 표시 or -1
//		rank[x] = 0; // 초기값 0임 // 생략 가능
	}

	/** 멤버 x를 포함하는 집합의 대표자를 리턴 */
	public static int findSet(int x) {
		if (x == p[x]) {
			return x;
		} else {
			p[x] = findSet(p[x]); // Path Compression
//			rank[x] = 1; // 할 필요가 없다. 대표자의 깊이(랭크)만 알면 된다.
			return p[x];
		}
	}

	/** 멤버 x,y의 대표자를 구해서 두 집합을 통합 */
	public static void unionSet(int x, int y) {
		int px = findSet(x); // 대표자 알아오기
		int py = findSet(y);

		if (px != py) { // 서로 다른 집합일 경우만 합쳐야한다. 무한루프가 돔
//			p[py] = px; // 두 집합의 대표자를 합치기
			link(px, py);
		}
	}

	/** x의 대표자의 집합과 y의 대표자의 집합을 합침, rank도 맞춤 */
	public static void link(int px, int py) {
		if (rank[px] > rank[py]) {
			p[py] = px; // 작은 쪽을 큰 쪽에 붙인다
		} else {
			p[px] = py;
			if (rank[px] == rank[py]) { // 같은 경우는 rank 값이 증가한다.
				rank[py]++; // 집합의 대표자 랭크가 증가됨
			}
		}
	}

	public static class Node {
		int index;
		int x;
		int y;

		public Node(int index, int x, int y) {
			this.index = index;
			this.x = x;
			this.y = y;
		}
	}

	public static class Edge implements Comparable<Edge> {
		int a; // 정점1
		int b; // 정점2
		double val; // 가중치

		public Edge(int a, int b, double val) {
			this.a = a;
			this.b = b;
			this.val = val;
		}

		@Override
		public int compareTo(Edge o) { // 비교 기준
			if(this.val - o.val < 0) {
				return -1;
			}else if(this.val - o.val > 0) {
				return 1;
			}else {
				return 0;
			}
		}
	}
} // end of class
