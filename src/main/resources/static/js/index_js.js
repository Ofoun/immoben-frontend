/**
 * 
 */
moduleURL = "[[@{/}]]";

$(document).ready(
	function() {
		$(".link-delete").on("click", function(e) {
			e.preventDefault();
			showDeleteConfirmModal($(this), 'product');
		});

		$(".link-detail").on(
				"click",
				function(e) {
					e.preventDefault();
					linkDetailURL = $(this).attr("href");
					$("#detailModal").modal("show").find(
							".modal-content").load(
							linkDetailURL);
				});

		/*  $("#dropdownCategory").on("change", function() {
				$("#searchForm").submit();
			});
		
		    $("#dropdownCity").on("change", function() {
		        $("#searchForm").submit();
		    });   */

	});