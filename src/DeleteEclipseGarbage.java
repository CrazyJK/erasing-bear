import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;


/**
 * git폴더에 eclipse프로젝트 관련 파일을 모두 지우기
 * @author kamoru
 *
 */
public class DeleteEclipseGarbage {

	private static final String INDENT1 = "  ";
	private static final String INDENT2 = INDENT1 + "  ";
	private static final String INDENT3 = INDENT2 + "  ";
	private static final String[] garbageNames = new String[]{".project", ".classpath", ".settings"};
	
	/**
	 * basepath 하위 폴더의 eclipse 관련 파일 삭제
	 * @param basepath
	 */
	private static void process(String basepath) {
		System.out.format("%s - %s%n", "Delete Eclipse Garbage", basepath);
		
		// 현재 폴더의 하위 폴더만 찾는다
		File baseDir = new File(basepath);
		File[] subDirs = baseDir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.isDirectory())
					return true;
				return false;
			}});
		System.out.format("%sFound %s directories%n", INDENT1, subDirs.length);
		
		// 하위폴더 하나하나에 대해
		for (File dir : subDirs) {
			System.out.format("%sDirectory : %s%n", INDENT2, dir.getName());
			// 하위폴더에서 eclipse관련 파일을 찾는다
			File[] files = dir.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
//					System.out.format("%s- %s%n",indent + indent, name);
					for (String garbageName : garbageNames) {
						if (garbageName.equals(name))
							return true;
					}
					return false;
				}});

			// 찾아낸 eclipse관련 파일을 삭제한다
			for (File file : files) {
				boolean deleted = deleteDirectory(file);
				System.out.format("%sAttempt to delete : %-20s [%s]%n", INDENT3, file.getName(), deleted);
			}
		}
	}

	/**<pre>
	 * 폴더/파일을 삭제한다. 
	 * 폴더속 파일이 있으면 같이 삭제.
	 * path가 파일이어도 삭제
	 * </pre>
	 * @param path
	 * @return
	 */
	private static boolean deleteDirectory(File path) {
		if (!path.exists())
			return false;

		File[] files = path.listFiles();
		if (files != null) 
			for (File file : files)
				if (file.isDirectory())
					deleteDirectory(file);
				else
					file.delete();
		return path.delete();
	}

	public static void main(String[] args) {
		if (args == null || args.length < 1)
			DeleteEclipseGarbage.process(".");
		else
			DeleteEclipseGarbage.process(args[0]);
	}

}
