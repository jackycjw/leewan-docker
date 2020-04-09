package com.leewan.util;

public class DocumentAuthUtils {

	/**
	 * 生产文档权限值
	 * 权限位1-3为内部，存在向上开放的兼容性，如对测试人员（3）开放，则管理员(1)，开发人员(2)肯定也可以看到
	 * 权限值大于3为对外开放，这种情况下 只对1-3兼容，其它角色不兼容
	 * @param digit
	 * @return
	 */
	public static long generateDocumentAuth(int digit) {
		long result = 0;
		for(int i = 0;i < digit;i++) {
			if(i < 3) {
				result += Math.pow(2, i);
			} 
			else if(i == digit - 1) {
				result += Math.pow(2, i);
			}
		}
		return result;
	}
	
	
	public static boolean isOpen(long auth, int digit) {
		for(int i=1;i<digit;i++) {
			auth = auth >> 1;
		}
		return (auth & 1) == 1;
	}
	
	public static void main(String[] args) {
		long r = generateDocumentAuth(5);
		r = r | generateDocumentAuth(7);
		r = r | generateDocumentAuth(63);
		
		for(int i=1;i<=10000;i++) {
			boolean open = isOpen(r, i);
			if(open) {
				System.out.println(i);
			}
		}
	}
}
